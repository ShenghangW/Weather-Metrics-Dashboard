package app;

public class RegionSummary {
    private String region;
    private int stationCount;
    private double averageMetric;

    public RegionSummary(String region, int stationCount, double averageMetric) {
        this.region = region;
        this.stationCount = stationCount;
        this.averageMetric = averageMetric;
    }

    // Getters
    public String getRegion() { return region; }
    public int getStationCount() { return stationCount; }
    public double getAverageMetric() { return averageMetric; }
}