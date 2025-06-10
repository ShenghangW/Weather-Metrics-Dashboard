package app;

public class TeamMember {
    private String name;
    private String studentNumber;
    private String subtask;
    private String role;

    public TeamMember(String name, String studentNumber, String subtask, String role) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.subtask = subtask;
        this.role = role;
    }

    public String getName() { return name; }
    public String getStudentNumber() { return studentNumber; }  // Fixed method name
    public String getSubtask() { return subtask; }
    public String getRole() { return role; }
}