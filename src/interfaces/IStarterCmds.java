package interfaces;

import data.*;

public interface IStarterCmds
{
    boolean SubmitProject(String email, PaymentInfo paymentInfo, String name, String desc,
            double target, DateTime endDate, Reward[] rewards);

    void ForwardConfirmProject(int pId);
}
