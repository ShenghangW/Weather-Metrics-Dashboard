package app;

public class QUALITY {
    private String location;
    private String name;
    private String date;
    private String metricValue;

    public QUALITY(String location, String name, String date, String metricValue){
        this.location = location;
        this.name = name;
        this.date = date;
        this.metricValue = metricValue;
    }

    public String getField() {
        return location;
    }

    public String getname() {
        return name;
    }

    public String getdate() {
        return date;
    }

    public String getMetricValue() {
        return metricValue;
    }
}
