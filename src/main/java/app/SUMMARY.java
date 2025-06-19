package app;

public class SUMMARY {
    private String flagName;
    private String flagNumber;

    public SUMMARY(String flagName, String flagNumber){
        this.flagName = flagName;
        this.flagNumber = flagNumber;
    }

    public String getFlagName() {
        return flagName;
    }

    public String getFlagNumber() {
        return flagNumber;
    }
}
