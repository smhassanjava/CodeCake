package interfaces;

import data.*;

public interface ISupporterCmds
{
    boolean SubmitDonation(int pId, String email, PaymentInfo paymentInfo, double amount);

    void ForwardConfirmDonation(int dId);

    Project[] RequestProjectList(Status status);

    Project RequestProjectDescription(int pId);
}