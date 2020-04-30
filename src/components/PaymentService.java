package components;

import data.*;
import interfaces.*;

public class PaymentService implements IPaymentService
{
    private static PaymentService instance;

    public static PaymentService GetInstance()
    {
        // Lazy instantiation and singleton implementation.
        return instance == null ? instance = new PaymentService() : instance;
    }

    public static void SetInstance(PaymentService instance)
    {
        // Allow altering singleton instance for testing.
        PaymentService.instance = instance;
    }

    @Override
    public void ChargeDonation(PaymentInfo sender, PaymentInfo receiver, double amount)
    {
        // No-Impl: immediately receive
        PF_Application.GetInstance().ChargeReceived(sender, receiver, amount);
    }

    @Override
    public void TransferDonation(PaymentInfo sender, PaymentInfo receiver, double amount)
    {
        // No-Impl: output transfer info
        System.out.println();
        System.out.println("[Donation Transferred]");
        System.out.println("  From: " + sender.userId);
        System.out.println("  To:   " + receiver.userId);
        System.out.println();
    }
}
