package components;

import data.*;
import interfaces.*;

public class EmailAdapter implements IEmail
{
    private static EmailAdapter instance;
    
    public static EmailAdapter GetInstance()
    {
        // Lazy instantiation and singleton implementation.
        return instance == null ? instance = new EmailAdapter() : instance;
    }

    public static void SetInstance(EmailAdapter instance)
    {
        // Allow altering singleton instance for testing.
        EmailAdapter.instance = instance;
    }

    @Override
    public void SendProjectConfirmLink(String email, String link, int pId)
    {
        // No-Impl: display confirmation email in output stream
        System.out.println();
        System.out.println("[Project Confirmation Link]");
        System.out.println("  Receiver: " + email);
        System.out.println("  Link:     " + link);
        System.out.println();
    }

    @Override
    public void SendDonationConfirmLink(String email, String link, int dId)
    {
        // No-Impl: display confirmation email in output stream
        System.out.println();
        System.out.println("[Donation Confirmation Link]");
        System.out.println("  Receiver: " + email);
        System.out.println("  Link:     " + link);
        System.out.println();
    }

    @Override
    public void NotifyStatusStarter(String email, String name, Status status)
    {
        // No-Impl: display notification email in output stream
        System.out.println();
        System.out.println("[Project Status Starter Notification]");
        System.out.println("  Receiver: " + email);
        System.out.println("  Project:  " + name);
        System.out.println("  Status:   " + status.name());
        System.out.println();
    }

    @Override
    public void NotifyStatusSupporter(String email, String name, Status status)
    {
        // No-Impl: display notification email in output stream
        System.out.println();
        System.out.println("[Project Status Supporter Notification]");
        System.out.println("  Receiver: " + email);
        System.out.println("  Project:  " + name);
        System.out.println("  Status:   " + status.name());
        System.out.println();
    }
}
