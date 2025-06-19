package app;

public class MetricData {
    private String metricValue;
    private String date;
    private String location;
    
    // Corrected parameter order: value, date, location
    public MetricData(String metricValue, String date, String location) {
        this.metricValue = metricValue;
        this.date = date;
        this.location = location;
    }

    public String getMetricValue() { return metricValue; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
}