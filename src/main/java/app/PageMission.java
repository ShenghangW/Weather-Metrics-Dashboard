package app;

import java.util.ArrayList;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageMission implements Handler {
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "<html><head><title>Our Mission</title>"
                + "<link rel='stylesheet' type='text/css' href='missionPage.css' />"
                + "</head><body>"

                + "<div class='topnav'>"
                    + "<a href='/'>Homepage</a>"
                    + "<a href='mission.html'>Our Mission</a>"
                    + "<a href='page2A.html'>Sub Task 2.A</a>"
                    + "<a href='page2B.html'>Sub Task 2.B</a>"
                    + "<a href='page2C.html'>Sub Task 2.C</a>"
                    + "<a href='page3A.html'>Sub Task 3.A</a>"
                    + "<a href='page3B.html'>Sub Task 3.B</a>"
                    + "<a href='page3C.html'>Sub Task 3.C</a>"
                + "</div>"
                
                + "<br>"
                + "<div class='pageRef'>"
                    + "<a href='/'>Home/</a>"
                    + "<a href='mission.html'>About us</a>"
                + "</div>"
                
                + "<div><h1 class='missionHeader'>About us</h1></div>"
                + "<div class>"
                
                + "<br>"
                + "<h2 class='head1'>Our mission</h2>"
                + "<p class='paragraph'>Climate change transcends borders, sectors, and generations, demanding collective action grounded in clarity. "
                + "Our platform confronts this global crisis by democratizing access to authoritative, up-to-date climate data spanning 1970 to 2022 enabling users to trace evolving patterns, "
                + "validate trends, and make informed decisions. We bridge the gap between complex climate science and practical resilience, equipping stakeholders with the foundational insights needed to mitigate risks, "
                + "adapt strategies, and drive systemic change. By transforming raw data into actionable knowledge, we empower society to navigate uncertainty with confidence.</p>"
                
                + "<br><br>"
                + "<h3>How to Use This Site</h3>"
                + "<ul>"
                + "<li><strong>Explore Data</strong>: Navigate through climate metrics using our interactive tools.</li>"
                + "<li><strong>Compare Regions</strong>: Analyze climate differences through viewing different weather station in all States.</li>"
                + "<li><strong>Track Trends</strong>: Visualize changes over time with our graphing tools.</li>"
                + "</ul>";

        // FIXED TYPO IN CLASS NAME
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<Persona> personas = jdbc.getPersonas();

        html += "<br><br>";
        html += "<h2 class='head1'>Target Personas</h2><ul>";
        for (Persona persona : personas) {
            html += "<li><strong>" + persona.getName() + "</strong>: "
                + persona.getDescription() + "</li>";
        }
        html += "</ul>";

        ArrayList<TeamMember> teamMembers = jdbc.getTeamMembers();

        html += "<br><br>";
        html += "<h2 class='head1'>Our Team</h2>"
            + "<table style='width:100%; border-collapse: collapse;'>"
            + "<tr style='background-color: #333; color: white;'>"
            + "<th>Name</th><th>Student ID</th><th>Subtask</th><th>Role</th>"
            + "</tr>";

        for (TeamMember member : teamMembers) {
            html += "<tr style='border-bottom: 1px solid #ddd;'>"
                + "<td class=''>" + member.getName() + "</td>"
                + "<td>" + member.getStudentNumber() + "</td>"
                + "<td>" + member.getSubtask() + "</td>"
                + "<td>" + member.getRole() + "</td>"
                + "</tr>";
        }

        html += "</table>"
            + "</div>" 
            + "<div class='footer'>"
            + "<p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>"
            + "</div></body></html>"; 

        context.html(html);
    }
}