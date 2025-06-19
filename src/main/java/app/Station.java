package app;

public class Station {
    private String site;
    private String name;
    private String state;
    private String region;
    private double latitude;
    private double longitude;
    private String metric;

    public Station(String site, String name, String state, double latitude, double longitude, String metric) {
        this.site = site;
        this.name = name;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.metric = metric;

    }

    // Getters
    public String getSite() { return site; }
    public String getName() { return name; }
    public String getState() { return state;}
    public double getLat() { return latitude; }
    public double getlongt() { return longitude;}
    public String getmetric() { return metric;}
}
