package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageData implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/data.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Our Data</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + PageIndex.navbar;

        // Add header content block
        html = html + """
            <div class='dataHeader'>
                <h1>Data</h1>
            </div>
        """;

        // Uses JDBC to lookup data
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<METADATA> metadata = jdbc.getMetadata();

        ArrayList<String> headings = new ArrayList<String>();
        ArrayList<Integer> indices = new ArrayList<Integer>();

        headings.add("Precipitation");
        headings.add("Evaporation");
        headings.add("Temperature");
        headings.add("Humidity");
        headings.add("Sunshine");
        headings.add("Cloud Coverage");

        int precipIndex = 2;
        int evapIndex = 6;
        int tempIndex = 9;
        int humIndex = 15;
        int sunIndex = 31;
        int cloudIndex = 33;

        indices.add(precipIndex);
        indices.add(evapIndex);
        indices.add(tempIndex);
        indices.add(humIndex);
        indices.add(sunIndex);
        indices.add(cloudIndex);
        indices.add(metadata.size());

        // Add Div for page Content
        html = html + "<div class='content'>";

        html += "<h1>Summary of our data</h1>";
        html +=     "<p>Our data contains weather data from 141 weather stations across 6 Australian states and territories (excluding A.C.T.) " +
                    "as well as stations in antarctic and extended territories (including Maquarie and Norfolk islands).</p>" +
                    "<p>We collect 6 categories of environmental weather data:</p>" +
                    "<ol>" +
                    "<li>Precipitation</li>" +
                    "<li>Evaporation</li>" +
                    "<li>Temperature</li>" +
                    "<li>Humidity</li>" +
                    "<li>Sunshine</li>" +
                    "<li>Cloud Coverage</li>" +
                    "</ol>" +
                    "<p>Every data point contains 3 pieces of identifying information:</p>" +
                    "<ul>" +
                    "<li><b>MeasurementId:</b> A unique ID number for every data point in our dataset, in the format XYYYYYYY where X represents the " +
                    "category it belongs to according to the list above, and Y represents the unique ID for each category.</li>" +
                    "<li><b>" + metadata.get(0).getField() + ":</b> " + metadata.get(0).getDescription() + "</li>" +
                    "<li><b>" + metadata.get(1).getField() + ":</b> " + metadata.get(1).getDescription() + "</li>" +
                    "</ul>" +
                    "<p>Each category of data contains various related measurements, which is described in detail in the following tables:</p>";
        
        for (int i = 0 ; i < headings.size() ; ++i){
            html += "<h2>" + headings.get(i) + "</h2>" +
                    "<table class='descTables'>" +
                    "<tr>" +
                    "<th class='descTables'>Data Name</th>" +
                    "<th class='descTables'>Description</th>" +
                    "</tr>";

            for(int j = indices.get(i) ; j < indices.get(i+1) ; ++j){
                html += "<tr><td class='descTables'>" +
                        metadata.get(j).getField() +
                        "</td><td class='descTables'>" +
                        metadata.get(j).getDescription() +
                        "</td></tr>";
            }

            html += "</table>";
        }

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

}
