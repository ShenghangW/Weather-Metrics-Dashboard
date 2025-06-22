package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageCorrelation implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/correlation.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" +
                "<title>Data Correlation</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        html = html + PageIndex.navbar;

        // Add header content block
        html = html + """
                    <div class='correlationHeader'>
                        <h1>Data Correlation</h1>
                    </div>
                """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
            <p>This page can be used to investigate correlation between weather metrics. Please select a reference weather 
            station and the metric of interest. Results can be grouped over various time periods.</p>
            """;

        html += PageDataQuality.dropScript;

        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<STATE> stateList = jdbc.getStates();
        ArrayList<SITE> siteList = getSite();

        html += "<form action='/correlation' method='post'>" +
                "<label for='sites'>Select Weather Station:</label>" +
                "<select name='sites' id='sites'>";

        int siteNum = 0;

        for (int i = 0; i < stateList.size(); ++i) {
            String stateAbbv = stateList.get(i).getState();
            html += "<optgroup label='" + stateList.get(i).getStateName() + "'>";
            for (int j = siteNum; stateAbbv.equals(siteList.get(j).getState()) ; ++j) {
                html += "<option value='" + siteList.get(j).getSite() + "'>" + siteList.get(j).getSiteName() + "</option>";
                siteNum = j+1;
            }
            html += "</optgroup>";
        }

        html += "</form>";

        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
            <div class='footer'>
                <p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>
            </div>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    
    public ArrayList<SITE> getSite() {

        ArrayList<SITE> sites = new ArrayList<SITE>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "select site, name, state from location order by state";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String site = results.getString("site");
                String siteName = results.getString("name");
                String state = results.getString("state");

                SITE sitesObj = new SITE(site, siteName, state);

                sites.add(sitesObj);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return sites;
    }

}
