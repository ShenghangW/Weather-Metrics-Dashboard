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

        html += "<form action='/correlation' method='post'>";

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

        ArrayList<FLAG> flags = new ArrayList<FLAG>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "select site, state from location order by state";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String site = results.getString("site");
                String state = results.getString("state");

                // Create an FLAG Object
                SITE sitesObj = new SITE(site, state);

                // Add the FLAG object to the flags array
                flags.add(flagsObj);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return flags;
    }

}
