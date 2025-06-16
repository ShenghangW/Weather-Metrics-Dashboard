package app;

public class METADATA {
    private String field;
    private String description;

    public METADATA(String field,String description){
        this.field = field;
        this.description = description;
    }

    public String getField(){
        return field;
    }

    public String getDescription(){
        return description;
    }
}
