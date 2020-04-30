package interfaces;

import data.*;

public interface IPaymentCmds
{
    public void ChargeReceived(PaymentInfo sender, PaymentInfo receiver, double amount);
}
