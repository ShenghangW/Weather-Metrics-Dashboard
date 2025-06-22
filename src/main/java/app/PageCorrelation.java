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

        html += "<form action='/correlation' method='post'>";

        html += jdbc.stationDrop();

        html += "<br>" + PageDataQuality.metricDrop;

        html += """
                <label for='period'>Group data by: </label>
                <select name='period' id='period' required>
                <option value='' disabled selected>Time period</option>
                <option value='Week'>1 Week</option>
                <option value='Month'>1 Month</option>
                <option value='Quarter'>3 Months</option>
                <option value='Half'>6 Months</option>
                <option value='Year'>1 Year</option>
                <option value='HalfDecade'>5 Years</option>
                <option value='Decade'>10 years</option>
                </select>
                </div>
                <br>
                <div class='form-group'>
                <label for="startDate">Start date:</label>
                <input type="date" id="startDate" name="startDate" placeholder="dd/mm/yyyy" required></input><br>
                </div>
                """;

        html += "</form>";

        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
            <div class='footer'>
                <p>Weather Report(2025)</p>
            </div>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    
    public static ArrayList<SITE> getSite() {

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
                String siteNameUpper = results.getString("name");
                String state = results.getString("state");

                String siteNameLower = siteNameUpper.toLowerCase();
                String siteName = "";
                siteName += String.valueOf(siteNameLower.charAt(0)).toUpperCase();

                for (int i = 1 ; i < siteNameLower.length() ; ++i) {
                    siteName += String.valueOf(siteNameLower.charAt(i));
                    if (((siteNameLower.charAt(i) == ' ') && (siteNameLower.charAt(i+1) != '(')) || (siteNameLower.charAt(i) == '(')) {
                        siteName += String.valueOf(siteNameLower.charAt(i+1)).toUpperCase();
                        ++i;
                    }
                }

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
