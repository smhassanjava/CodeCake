package components;

import java.sql.*;
import java.util.*;
import data.*;
import interfaces.*;

public class DBFacade implements IDatabase
{
    private static DBFacade instance;

    public DBFacade()
    {
        // Register the driver.
        try
        {
            Class.forName("com." + DBConfig.Type + ".cj.jdbc.Driver").newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static DBFacade GetInstance()
    {
        // Lazy instantiation and singleton implementation.
        return instance == null ? instance = new DBFacade() : instance;
    }

    public static void SetInstance(DBFacade instance)
    {
        // Allow altering singleton instance for testing.
        DBFacade.instance = instance;
    }

    @Override
    public void AddProject(Project project)
    {
        assert project != null;
        assert project.rewards != null;
        // Connect to the database.
        try (Connection conn = DriverManager.getConnection(DBConfig.GetUrl(), DBConfig.User, DBConfig.Password))
        {
            // Insert the new project.
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO `projects` (`email`, `paymentInfo`, `name`, `desc`, `current`, `target`, `endDate`, `status`) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);"))
            {
                ps.setString(1, project.email);
                ps.setString(2, project.paymentInfo.userId);
                ps.setString(3, project.name);
                ps.setString(4, project.desc);
                ps.setDouble(5, project.current);
                ps.setDouble(6, project.target);
                ps.setDate(7, new java.sql.Date(project.endDate.value));
                ps.setString(8, project.status.name());
                ps.executeUpdate();
            }
            // Insert all associated rewards.
            for (Reward r : project.rewards)
            {
                try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO `rewards` (`pId`, `tier`, `reward`) "
                        + "VALUES (?, ?, ?);"))
                {
                    ps.setInt(1, project.pId);
                    ps.setDouble(2, r.tier);
                    ps.setString(3, r.reward);
                    ps.executeUpdate();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void AddDonator(Donation donation)
    {
        assert donation != null;
        // Connect to the database.
        try (Connection conn = DriverManager.getConnection(DBConfig.GetUrl(), DBConfig.User, DBConfig.Password))
        {
            // Insert the new donation.
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO `donations` (`pId`, `email`, `paymentInfo`, `amount`) "
                    + "VALUES (?, ?, ?, ?);"))
            {
                ps.setInt(1, donation.pId);
                ps.setString(2, donation.email);
                ps.setString(3, donation.paymentInfo.userId);
                ps.setDouble(4, donation.amount);
                ps.executeUpdate();
            }
            // Update the associated project.
            try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE `projects` "
                    + "SET `current` = `current` + ? "
                    + "WHERE `pId` = ?;"))
            {
                ps.setDouble(1, donation.amount);
                ps.setInt(2, donation.pId);
                ps.executeUpdate();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Project GetProject(int pId)
    {
        // Connect to the database.
        try (Connection conn = DriverManager.getConnection(DBConfig.GetUrl(), DBConfig.User, DBConfig.Password))
        {
            // Query the project.
            try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM `projects` "
                    + "WHERE `pId` = ?;"))
            {
                ps.setInt(1, pId);
                return GetProjectsInternal(conn, ps)[0];
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Project[] GetProjects(Status status)
    {
        // Connect to the database.
        try (Connection conn = DriverManager.getConnection(DBConfig.GetUrl(), DBConfig.User, DBConfig.Password))
        {
            // Query the projects.
            try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM `projects` "
                    + "WHERE `status` = ?;"))
            {
                ps.setString(1, status.name());
                return GetProjectsInternal(null, ps);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Project[] GetProjectsExpired()
    {
        // Connect to the database.
        try (Connection conn = DriverManager.getConnection(DBConfig.GetUrl(), DBConfig.User, DBConfig.Password))
        {
            // Query the projects.
            try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM `projects` "
                    + "WHERE `endDate` <= CAST(NOW() AS DATE);"))
            {
                return GetProjectsInternal(null, ps);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private Project[] GetProjectsInternal(Connection conn, PreparedStatement ps)
        throws SQLException
    {
        assert ps != null;
        // Create projects from the query result.
        ArrayList<Project> projects = new ArrayList<>();
        ResultSet res = ps.executeQuery();
        while (res.next())
        {
            Project p = new Project(
                res.getInt("pId"),
                res.getString("email"),
                new PaymentInfo(res.getString("paymentInfo")),
                res.getString("name"),
                res.getString("desc"),
                res.getDouble("current"),
                res.getDouble("target"),
                new DateTime(res.getDate("endDate").getTime()),
                Status.valueOf(res.getString("status")),
                null);
            // Retrieve rewards if requested.
            if (conn != null)
            {
                try (PreparedStatement psRewards = conn.prepareStatement(
                    "SELECT * FROM `rewards` "
                        + "WHERE `pId` = ?;"))
                {
                    psRewards.setInt(1, p.pId);
                    ResultSet res2 = psRewards.executeQuery();
                    ArrayList<Reward> rewards = new ArrayList<>();
                    while (res2.next())
                    {
                        rewards.add(new Reward(
                            res2.getDouble("tier"),
                            res2.getString("reward")));
                    }
                    p.rewards = rewards.toArray(new Reward[rewards.size()]);
                }
            }
            projects.add(p);
        }
        return projects.toArray(new Project[projects.size()]);
    }

    @Override
    public Donation[] GetDonations(int pId)
    {
        // Connect to the database.
        try (Connection conn = DriverManager.getConnection(DBConfig.GetUrl(), DBConfig.User, DBConfig.Password))
        {
            // Query the donations.
            try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM `donations` "
                    + "WHERE `pId` = ?;"))
            {
                ps.setInt(1, pId);
                ResultSet res = ps.executeQuery();
                // Create donations from the query result.
                ArrayList<Donation> donations = new ArrayList<>();
                while (res.next())
                {
                    donations.add(new Donation(
                        res.getInt("dId"),
                        res.getInt("pId"),
                        res.getString("email"),
                        new PaymentInfo(res.getString("paymentInfo")),
                        res.getDouble("amount")));
                }
                return donations.toArray(new Donation[donations.size()]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void SetStatus(int pId, Status status)
    {
        // Connect to the database.
        try (Connection conn = DriverManager.getConnection(DBConfig.GetUrl(), DBConfig.User, DBConfig.Password))
        {
            // Update the status of the project.
            try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE projects "
                    + "SET status = ? "
                    + "WHERE pId = ?;"))
            {
                ps.setString(1, status.name());
                ps.setInt(2, pId);
                ps.executeUpdate();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
