package app;

public class RegionSummary {
    private String regionName;
    private int stationCount;
    private double averageMetric;

    public RegionSummary(String regionName, int stationCount, double averageMetric) {
        this.regionName = regionName;
        this.stationCount = stationCount;
        this.averageMetric = averageMetric;
    }

    public String getRegionName() {
        return regionName;
    }

    public int getStationCount() {
        return stationCount;
    }

    public double getAverageMetric() {
        return averageMetric;
    }
}