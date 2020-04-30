package interfaces;

import data.*;

public interface IEmail
{
    void SendProjectConfirmLink(String email, String link, int pId);

    void SendDonationConfirmLink(String email, String link, int dId);

    void NotifyStatusStarter(String email, String name, Status status);

    void NotifyStatusSupporter(String email, String name, Status status);
}
