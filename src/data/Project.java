package data;

public class Project {
    public int pId;
    public String email;
    public PaymentInfo paymentInfo;
    public String name;
    public String desc;
    public double current;
    public double target;
    public DateTime endDate;
    public Status status;
    public Reward[] rewards;

    public Project(int pId, String email, PaymentInfo paymentInfo, String name, String desc, double current, 
            double target, DateTime endDate, Status status, Reward[] rewards)
    {
        this.pId = pId;
        this.email = email;
        this.paymentInfo = paymentInfo;
        this.name = name;
        this.desc = desc;
        this.current = 0.0;
        this.target = target;
        this.endDate = endDate;
        this.status = Status.Open;
        this.rewards = rewards;
    }

    public int getPid()
    {
        return pId;
    }

    public String getEmail()
    {
        return email;
    }

    public String getName()
    {
        return name;
    }

    public String getDesc()
    {
        return desc;
    }

    public double getCurrent()
    {
        return current;
    }

    public double getTarget()
    {
        return target;
    }
}