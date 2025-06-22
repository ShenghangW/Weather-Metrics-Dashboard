package app;

public class SimilarStation {
    private String site;
    private String name;
    private double period1Avg;
    private double period2Avg;
    private double percentChange;
    private double similarityScore;

    public SimilarStation(String site, String name, double period1Avg, double period2Avg, double percentChange, double similarityScore) {
        this.site = site;
        this.name = name;
        this.period1Avg = period1Avg;
        this.period2Avg = period2Avg;
        this.percentChange = percentChange;
        this.similarityScore = similarityScore;
    }

    public String getSite() {
        return site;
    }

    public String getName() {
        return name;
    }

    public double getPeriod1Avg() {
        return period1Avg;
    }

    public double getPeriod2Avg() {
        return period2Avg;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }
}