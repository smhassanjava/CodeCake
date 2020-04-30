package data;

public class Reward {
    public double tier;
    public String reward;

    public Reward(double tier, String reward)
    {
        this.tier = tier;
        this.reward = reward;
    }

    public double getTier()
    {
        return tier;
    }

    public String getReward()
    {
        return reward;
    }
}
