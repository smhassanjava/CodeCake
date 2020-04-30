package data;

public class Donation {
    public int dId;
    public int pId;
    public String email;
    public PaymentInfo paymentInfo;
    public double amount;

    public Donation(int dId, int pId, String email, PaymentInfo paymentInfo, double amount)
    {
        this.dId = dId;
        this.pId = pId;
        this.email = email;
        this.paymentInfo = paymentInfo;
        this.amount = amount;
    }
}