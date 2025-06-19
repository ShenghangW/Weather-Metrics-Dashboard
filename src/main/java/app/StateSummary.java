package app;

public class StateSummary {
    private String state;
    private double totalValue;

    public StateSummary(String state, double totalValue) {
        this.state = state;
        this.totalValue = totalValue;
    }

    public String getState() { return state; }
    public double getTotalValue() { return totalValue; }
}
