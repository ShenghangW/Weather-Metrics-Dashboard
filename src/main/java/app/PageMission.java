package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageMission implements Handler {
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "<html><head><title>Our Mission</title>"
                + "<link rel='stylesheet' type='text/css' href='common.css' />"
                + "</head><body>"

                + PageIndex.navbar
                
                + "<br>"
                + "<div class='pageRef'>"
                    + "<a href='/'>Home/</a>"
                    + "<a href='mission.html'>About us</a>"
                + "</div>"
                    
                + "<div><h1 class='missionHeader'>About us</h1></div>";
                
                // Our Mission Section
    html += "<div class='mission-section'>"
        + "<div class='mission-overlay'>"
        + "<h2 class='mission-title'>Our Mission</h2>"
        + "<p class='mission-text'>Climate change transcends borders, sectors, and generations, demanding collective action grounded in clarity. "
        + "Our platform confronts this global crisis by democratizing access to authoritative, up-to-date climate data spanning 1970 to 2022 enabling users to trace evolving patterns, "
        + "validate trends, and make informed decisions. We bridge the gap between complex climate science and practical resilience, equipping stakeholders with the foundational insights needed to mitigate risks, "
        + "adapt strategies, and drive systemic change. By transforming raw data into actionable knowledge, we empower society to navigate uncertainty with confidence.</p>"
        + "</div>"
        + "</div>"; // Close mission-section

    // How to Use Section
    html += "<div class='how-to-section'>"
        + "<div class='how-to-overlay'>"
        + "<h2 class='how-to-title'>How to Use This Site</h2>"
        + "<ul class='how-to-list'>"
        + "<li><strong>Explore Data</strong>: Navigate through climate metrics using our interactive tools.</li>"
        + "<li><strong>Compare Regions</strong>: Analyze climate differences by viewing different weather stations across all states.</li>"
        + "<li><strong>Track Trends</strong>: Visualize changes over time with our graphing tools.</li>"
        + "</ul>"
        + "</div>"
        + "</div>"; // Close how-to-section

        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<Persona> personas = jdbc.getPersonas();

        html += "<br><br>";
        html += "<h2 class='head2-left'>Target Personas</h2>";
        html += "<div class='personas-container'>"; // Main container

        // Left persona container
        if(personas.size() > 0) {
            html += "<div class='persona-box persona-1'>";
            html += "<div class='persona-image'></div>";
            html += "<h3>" + personas.get(0).getName() + "</h3>";
            html += "<p>" + personas.get(0).getDescription() + "</p>";
            html += "</div>";
        }

        // Persona 2 (Middle)
        if(personas.size() > 1) {
            html += "<div class='persona-box persona-2'>";
            html += "<div class='persona-image'></div>";
            html += "<h3>" + personas.get(1).getName() + "</h3>";
            html += "<p>" + personas.get(1).getDescription() + "</p>";
            html += "</div>";
        }

        // Persona 3 (Right)
        if(personas.size() > 2) {
            html += "<div class='persona-box persona-3'>";
            html += "<div class='persona-image'></div>";
            html += "<h3>" + personas.get(2).getName() + "</h3>";
            html += "<p>" + personas.get(2).getDescription() + "</p>";
            html += "</div>";
        }

        html += "</div>"; // Close personas-container

        ArrayList<TeamMember> teamMembers = jdbc.getTeamMembers();

        html += "<br><br>";
        html += "<h2 class='head2'>Our Team</h2>"
            + "<div class='team-table-container'>"
            + "<table class='team-table'>"
            + "<thead>"
            + "<tr>"
            + "<th>Name</th><th>Student ID</th><th>Subtask</th><th>Role</th>"
            + "</tr>"
            + "</thead>"
            + "<tbody>";

        for (TeamMember member : teamMembers) {
            html += "<tr>"
                + "<td>" + member.getName() + "</td>"
                + "<td>" + member.getStudentNumber() + "</td>"
                + "<td>" + member.getSubtask() + "</td>"
                + "<td>" + member.getRole() + "</td>"
                +"</tr>";
        }

        html += "</tbody></table></div>"; 

        html += "<div class='footer'>"
            + "<p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>"
            + "</div></body></html>"; 

        context.html(html);
    }
}