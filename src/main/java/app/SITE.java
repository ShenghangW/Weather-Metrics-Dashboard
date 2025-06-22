package app;

public class SITE {

    private String site;
    private String siteName;
    private String state;

    public SITE(String site, String siteName, String state) {
        this.site = site;
        this.siteName = siteName;
        this.state = state;
    }

    public String getSite() {
        return site;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getState() {
        return state;
    }
}
