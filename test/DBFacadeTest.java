import static org.mockito.Mockito.*;
import java.sql.*;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.*;
import org.powermock.api.mockito.*;
import org.powermock.core.classloader.annotations.*;
import org.powermock.modules.junit4.*;
import components.*;
import data.*;
import junit.framework.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DBFacade.class)
public class DBFacadeTest extends TestCase
{
    private static Project p;
    private static Donation d;
    private Connection conn;
    private PreparedStatement ps1;
    private PreparedStatement ps2;

    @BeforeClass
    public static void init()
    {
        // Initialize static variables.
        p = new Project(0,
            "aaa@email.com",
            new PaymentInfo("#aaa"),
            "Aaa",
            null,
            0.0,
            5000.0,
            new DateTime(new java.util.Date().getTime()),
            Status.Open,
            new Reward[]
            {
                new Reward(100.0, "r1")
            });
        d = new Donation(0,
            0,
            "bbb@email.net",
            new PaymentInfo("#bbb"),
            300.0);
    }

    @Before
    public void setup()
    {
        // Set up the test environment.
        PowerMockito.mockStatic(DriverManager.class);
        conn = mock(Connection.class);
        ps1 = mock(PreparedStatement.class);
        ps2 = mock(PreparedStatement.class);

        try
        {
            when(DriverManager.getConnection(DBConfig.GetUrl(), DBConfig.User, DBConfig.Password)).thenReturn(conn);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddProject()
    {
        try
        {
            // Set up return values.
            when(conn.prepareStatement(
                "INSERT INTO `projects` (`email`, `paymentInfo`, `name`, `desc`, `current`, `target`, `endDate`, `status`) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);")).thenReturn(ps1);
            when(conn.prepareStatement(
                "INSERT INTO `rewards` (`pId`, `tier`, `reward`) "
                    + "VALUES (?, ?, ?);")).thenReturn(ps2);

            DBFacade.GetInstance().AddProject(p);
            verify(ps1, times(1)).executeUpdate();
            verify(ps2, times(p.rewards.length)).executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddDonator()
    {
        try
        {
            // Set up return values.
            when(conn.prepareStatement(
                "INSERT INTO `donations` (`pId`, `email`, `paymentInfo`, `amount`) "
                    + "VALUES (?, ?, ?, ?);")).thenReturn(ps1);
            when(conn.prepareStatement(
                "UPDATE `projects` "
                    + "SET `current` = `current` + ? "
                    + "WHERE `pId` = ?;")).thenReturn(ps2);

            DBFacade.GetInstance().AddDonator(d);
            verify(ps1, times(1)).executeUpdate();
            verify(ps2, times(1)).executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProject()
    {
        try
        {
            // Set up the test environment.
            ResultSet res1 = mock(ResultSet.class);
            ResultSet res2 = mock(ResultSet.class);

            // Set up return values.
            when(conn.prepareStatement(
                "SELECT * FROM `projects` "
                    + "WHERE `pId` = ?;")).thenReturn(ps1);
            when(ps1.executeQuery()).thenReturn(res1);
            when(res1.next()).thenReturn(true, false);
            when(res1.getInt("pId")).thenReturn(p.pId);
            when(res1.getString("email")).thenReturn(p.email);
            when(res1.getString("paymentInfo")).thenReturn(p.paymentInfo.userId);
            when(res1.getString("name")).thenReturn(p.name);
            when(res1.getString("desc")).thenReturn(p.desc);
            when(res1.getDouble("current")).thenReturn(p.current);
            when(res1.getDouble("target")).thenReturn(p.target);
            when(res1.getDate("endDate")).thenReturn(new Date(p.endDate.value));
            when(res1.getString("status")).thenReturn(p.status.name());
            when(conn.prepareStatement(
                "SELECT * FROM `rewards` "
                    + "WHERE `pId` = ?;")).thenReturn(ps2);
            when(ps2.executeQuery()).thenReturn(res2);
            when(res2.next()).thenReturn(true, false);
            when(res2.getDouble("tier")).thenReturn(p.rewards[0].tier);
            when(res2.getString("reward")).thenReturn(p.rewards[0].reward);

            Project result = DBFacade.GetInstance().GetProject(p.pId);
            verify(ps1, times(1)).executeQuery();
            verify(ps2, times(1)).executeQuery();
            assertEquals(p.pId, result.pId);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProjects()
    {
        try
        {
            // Set up the test environment.
            ResultSet res1 = mock(ResultSet.class);

            // Set up return values.
            when(conn.prepareStatement(
                "SELECT * FROM `projects` "
                    + "WHERE `status` = ?;")).thenReturn(ps1);
            when(ps1.executeQuery()).thenReturn(res1);
            when(res1.next()).thenReturn(true, false);
            when(res1.getInt("pId")).thenReturn(p.pId);
            when(res1.getString("email")).thenReturn(p.email);
            when(res1.getString("paymentInfo")).thenReturn(p.paymentInfo.userId);
            when(res1.getString("name")).thenReturn(p.name);
            when(res1.getString("desc")).thenReturn(p.desc);
            when(res1.getDouble("current")).thenReturn(p.current);
            when(res1.getDouble("target")).thenReturn(p.target);
            when(res1.getDate("endDate")).thenReturn(new Date(p.endDate.value));
            when(res1.getString("status")).thenReturn(p.status.name());
            when(conn.prepareStatement(
                "SELECT * FROM `rewards` "
                    + "WHERE `pId` = ?;")).thenReturn(ps2);

            Project[] result = DBFacade.GetInstance().GetProjects(p.status);
            verify(ps1, times(1)).executeQuery();
            verify(ps2, never()).executeQuery();
            assertEquals(1, result.length);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProjectsExpired()
    {
        try
        {
            // Set up the test environment.
            ResultSet res1 = mock(ResultSet.class);

            // Set up return values.
            when(conn.prepareStatement(
                "SELECT * FROM `projects` "
                    + "WHERE `endDate` <= CAST(NOW() AS DATE);")).thenReturn(ps1);
            when(ps1.executeQuery()).thenReturn(res1);
            when(res1.next()).thenReturn(true, false);
            when(res1.getInt("pId")).thenReturn(p.pId);
            when(res1.getString("email")).thenReturn(p.email);
            when(res1.getString("paymentInfo")).thenReturn(p.paymentInfo.userId);
            when(res1.getString("name")).thenReturn(p.name);
            when(res1.getString("desc")).thenReturn(p.desc);
            when(res1.getDouble("current")).thenReturn(p.current);
            when(res1.getDouble("target")).thenReturn(p.target);
            when(res1.getDate("endDate")).thenReturn(new Date(p.endDate.value));
            when(res1.getString("status")).thenReturn(p.status.name());
            when(conn.prepareStatement(
                "SELECT * FROM `rewards` "
                    + "WHERE `pId` = ?;")).thenReturn(ps2);

            Project[] result = DBFacade.GetInstance().GetProjectsExpired();
            verify(ps1, times(1)).executeQuery();
            verify(ps2, never()).executeQuery();
            assertEquals(1, result.length);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDonations()
    {
        try
        {
            // Set up the test environment.
            ResultSet res1 = mock(ResultSet.class);

            // Set up return values.
            when(conn.prepareStatement(
                "SELECT * FROM `donations` "
                    + "WHERE `pId` = ?;")).thenReturn(ps1);
            when(ps1.executeQuery()).thenReturn(res1);
            when(res1.next()).thenReturn(true, false);
            when(res1.getInt("dId")).thenReturn(d.dId);
            when(res1.getInt("pId")).thenReturn(d.pId);
            when(res1.getString("email")).thenReturn(d.email);
            when(res1.getString("paymentInfo")).thenReturn(d.paymentInfo.userId);
            when(res1.getDouble("amount")).thenReturn(d.amount);

            Donation[] result = DBFacade.GetInstance().GetDonations(d.pId);
            verify(ps1, times(1)).executeQuery();
            assertEquals(1, result.length);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetStatus()
    {
        try
        {
            // Set up return values.
            when(conn.prepareStatement(
                "UPDATE projects "
                    + "SET status = ? "
                    + "WHERE pId = ?;")).thenReturn(ps1);

            DBFacade.GetInstance().SetStatus(p.pId, Status.Failed);
            verify(ps1, times(1)).executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
