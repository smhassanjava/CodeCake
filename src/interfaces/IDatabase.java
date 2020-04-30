package interfaces;

import data.*;

public interface IDatabase
{
    void AddProject(Project project);

    void AddDonator(Donation donation);
    
    Project GetProject(int pId);

    Project[] GetProjects(Status status);

    Project[] GetProjectsExpired();

    Donation[] GetDonations(int pId);

    void SetStatus(int pId, Status status);
}
