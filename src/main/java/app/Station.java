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
    public String getmetric() {
         if(metric==null || metric.trim().isEmpty()){
            return "No Data";
         }
         return getspecialMetric(metric);
        }

        public String getspecialMetric(String metric){
        switch (metric.toLowerCase()) {
            case "okta":
                return "Cloud Coverage";
            case "humid":
                return "Relative Humidity";
            default:
                return metric;
        }
    }
}
