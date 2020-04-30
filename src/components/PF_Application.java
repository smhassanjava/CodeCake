package components;

import java.util.*;
import data.*;
import interfaces.*;
import utils.*;

public class PF_Application implements IStarterCmds, ISupporterCmds, ITimer, IPaymentCmds
{
    private static PF_Application instance;

    public final ArrayList<Project> tempProjects;
    public final ArrayList<Donation> tempDonations;
    private int lastPId;
    private int lastDId;

    private PF_Application()
    {
        // Initialize fields.
        tempProjects = new ArrayList<>();
        tempDonations = new ArrayList<>();
        lastPId = 0;
        lastDId = 0;
    }

    public static PF_Application GetInstance()
    {
        // Lazy instantiation and singleton implementation.
        return instance == null ? instance = new PF_Application() : instance;
    }

    public static void SetInstance(PF_Application instance)
    {
        // Allow altering singleton instance for testing.
        PF_Application.instance = instance;
    }

    private static boolean IsDataValid(String email, PaymentInfo paymentInfo, String name, double target, DateTime endDate, Reward[] rewards)
    {
        // Check if entered fields are not empty.
        return !StringUtils.IsNullOrWhitespace(email)
            && !StringUtils.IsNullOrWhitespace(paymentInfo.userId)
            && !StringUtils.IsNullOrWhitespace(name)
            && target > 0.0;
    }

    private static boolean IsDataValid(String email, PaymentInfo paymentInfo, double amount)
    {
        // Check if entered fields are not empty.
        return !StringUtils.IsNullOrWhitespace(email)
            && !StringUtils.IsNullOrWhitespace(paymentInfo.userId)
            && amount > 0.0;
    }

    @Override
    public boolean SubmitProject(String email, PaymentInfo paymentInfo, String name, String desc,
        double target, DateTime endDate, Reward[] rewards)
    {
        assert paymentInfo != null;
        assert rewards != null;
        // Validate data.
        if (!IsDataValid(email, paymentInfo, name, target, endDate, rewards))
        {
            return false;
        }
        // Create a new project and add it to tempProjects.
        Project p = new Project(lastPId++, email, paymentInfo, name, desc, 0.0, target, endDate, Status.Open, rewards);
        tempProjects.add(p);
        // Generate and send a confirmation link.
        String link = WebConfig.GetUrl() + "StarterGUI?action=ConfirmProject&pId=" + p.pId;
        EmailAdapter.GetInstance().SendProjectConfirmLink(email, link, p.pId);
        return true;
    }

    @Override
    public boolean SubmitDonation(int pId, String email, PaymentInfo paymentInfo, double amount)
    {
        assert paymentInfo != null;
        // Validate data.
        if (!IsDataValid(email, paymentInfo, amount))
        {
            return false;
        }
        // Create a new donation and add it to tempDonations.
        Donation d = new Donation(lastDId++, pId, email, paymentInfo, amount);
        tempDonations.add(d);
        // Generate and send a confirmation link.
        String link = WebConfig.GetUrl() + "SupporterGUI?action=ConfirmDonation&dId=" + d.dId;
        EmailAdapter.GetInstance().SendDonationConfirmLink(email, link, d.dId);
        return true;
    }

    @Override
    public void ForwardConfirmProject(int pId)
    {
        assert tempProjects.stream().anyMatch(p -> p.pId == pId);
        // Look for the project with pId.
        for (int i = 0; i < tempProjects.size(); i++)
        {
            if (tempProjects.get(i).pId == pId)
            {
                // Remove from tempProjects and add to the database.
                DBFacade.GetInstance().AddProject(tempProjects.remove(i));
                break;
            }
        }
    }

    @Override
    public void ForwardConfirmDonation(int dId)
    {
        assert tempDonations.stream().anyMatch(d -> d.dId == dId);
        // Look for the donation with dId.
        for (int i = 0; i < tempDonations.size(); i++)
        {
            if (tempDonations.get(i).dId == dId)
            {
                // Remove from tempDonations and add to the database.
                Donation d = tempDonations.remove(i);
                DBFacade.GetInstance().AddDonator(d);
                break;
            }
        }
    }

    @Override
    public Project[] RequestProjectList(Status status)
    {
        // Return a list of projects with the specified status.
        return DBFacade.GetInstance().GetProjects(status);
    }

    @Override
    public Project RequestProjectDescription(int pId)
    {
        // Return the project with the pId.
        return DBFacade.GetInstance().GetProject(pId);
    }

    @Override
    public void CheckEndDates()
    {
        // Loop for each project past the end date.
        for (Project p : DBFacade.GetInstance().GetProjectsExpired())
        {
            // Set the project state.
            p.status = p.current >= p.target ? Status.Successful : Status.Failed;
            DBFacade.GetInstance().SetStatus(p.pId, p.status);
            // Notify the project starter.
            EmailAdapter.GetInstance().NotifyStatusStarter(p.email, p.name, p.status);
            // Loop for each donator.
            for (Donation d : DBFacade.GetInstance().GetDonations(p.pId))
            {
                // Notify the project donator.
                EmailAdapter.GetInstance().NotifyStatusSupporter(d.email, p.name, p.status);
                // Charge the donators on successful projects.
                if (p.status == Status.Successful)
                {
                    PaymentService.GetInstance().ChargeDonation(d.paymentInfo, p.paymentInfo, d.amount);
                }
            }
        }
    }

    @Override
    public void ChargeReceived(PaymentInfo sender, PaymentInfo receiver, double amount)
    {
        assert sender != null;
        assert receiver != null;
        // Transfer the donation to the project starter.
        PaymentService.GetInstance().TransferDonation(sender, receiver, amount);
    }
}
