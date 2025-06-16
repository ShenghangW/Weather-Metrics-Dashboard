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
        html += "<h2 class='head1'>Target Personas</h2>";
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