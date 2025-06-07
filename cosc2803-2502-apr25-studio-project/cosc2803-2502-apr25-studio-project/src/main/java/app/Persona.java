package app;

public class Persona {
    private String name;
    private String description;

    public Persona(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}