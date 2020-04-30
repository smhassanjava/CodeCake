package interfaces;

import data.*;

public interface IPaymentService
{
    void ChargeDonation(PaymentInfo sender, PaymentInfo receiver, double amount);

    void TransferDonation(PaymentInfo sender, PaymentInfo receiver, double amount);
}
