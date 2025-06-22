package app;

public class SimilarMetric {
    private String metric;
    private double period1;
    private double period2;
    private double change;
    private double diffFromMetric;

    public SimilarMetric(String metric, double period1, double period2, double change, double diff) {
        this.metric = metric;
        this.period1 = period1;
        this.period2 = period2;
        this.change = change;
        this.diffFromMetric = diff;
    }

    public String getMetric() { return metric; }
    public double getPeriod1() { return period1; }
    public double getPeriod2() { return period2; }
    public double getChange() { return change; }
    public double getDiff() { return diffFromMetric; }
}