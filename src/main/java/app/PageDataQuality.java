package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class PageDataQuality implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/dataquality.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" +
                "<title>Data Quality</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        html = html + PageIndex.navbar;

        // Add header content block
        html = html + """
                    <div class='qualityheader'>
                        <h1>Data Quality</h1>
                    </div>
                """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        html += """
                <h1>Assessing quality of data</h1>
                <p>Not all data is equal. Weather stations will do their best to ensure the validity and accuracy
                of all data they collect, but given the sheer amount of data, realistically there will be poor
                quality data. We manage this by assessing the value of each data point, as well as the measurement
                conditions, to classify all data with a quality flag.</p>
                <p>Each quality flag is listed below:</p>
                """;

        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<FLAG> flags = jdbc.getFlags();

        html += """
                <table class='descTables'>
                <tr>
                <th>Flag</th>
                <th>Description</th>
                </tr>
                """;

        for (FLAG flagsObj : flags) {
            String flagName = flagsObj.getFlag();
            String flagDesc = flagsObj.getDescription();

            html += "<tr class='descTables'><td class='descTables'>" + flagName +
                    "</td><td class='descTables'>" + flagDesc + "</td></tr>";
        }

        html += "</table>";

        html += """
                <script>
                function updateOptions() {
                const metric = document.getElementById('metric').value;
                const time = document.getElementById('time');
                time.innerHTML = ''; // Clear previous options

                const options = {
                    humid: [
                        { text: '12am', value: '00' },
                        { text: '3am', value: '03' },
                        { text: '6am', value: '06' },
                        { text: '9am', value: '09' },
                        { text: '12pm', value: '12' },
                        { text: '3pm', value: '15' },
                        { text: '6pm', value: '18' },
                        { text: '9pm', value: '21' }
                    ],
                    okta: [
                        { text: '12am', value: '00' },
                        { text: '3am', value: '03' },
                        { text: '6am', value: '06' },
                        { text: '9am', value: '09' },
                        { text: '12pm', value: '12' },
                        { text: '3pm', value: '15' },
                        { text: '6pm', value: '18' },
                        { text: '9pm', value: '21' }
                    ]
                };
                if (metric && options[metric]) {
                options[metric].forEach(function(item) {
                const option = document.createElement('option');
                option.text = item.text;
                option.value = item.value;
                time.appendChild(option);
                });
                document.getElementById('time-container').style.display = 'block';
                } else {
                document.getElementById('time-container').style.display = 'none';
                }
                }
                </script>
                """;

        if (context.method().equals("GET")) {
            // Add HTML for the page content
            html += """
                        <p>To take a closer look at the poor quality data, please select the following options:</p>
                        <form action="/dataquality.html" method="post">
                        <label for="flag"></label>
                        <select name="flag" id="flag" required>
                        <option value="" disabled selected>Select quality flag</option>
                    """;

            for (FLAG flagsObj : flags) {
                String flagName = flagsObj.getFlag();

                if (flagName.charAt(0) != 'Y') {
                    html += "<option value='" + flagName + "'>" + flagName + "</option>";
                }
            }

            html += """
                        </select>
                        <label for="metric"></label>
                        <select name="metric" id="metric" onchange="updateOptions()" required>
                        <option value="" disabled selected>Select dataset</option>
                        <option value="precipitation">Precipitation</option>
                        <option value="evaporation">Evaporation</option>
                        <option value="maxtemp">Temperature (Max)</option>
                        <option value="mintemp">Temperature (Min)</option>
                        <option value="humid">Humidity</option>
                        <option value="sunshine">Sunshine</option>
                        <option value="okta">Cloud Coverage</option>
                        </select>
                        <div id='time-container' style='display:none; margin-top:10px;'>
                        <label for='time'>Select time of day:</label>
                        <select name='time' id='time'></select>
                        </div><br>
                        <label for="startDate">Please enter start date:</label>
                        <input type="date" id="startDate" name="startDate" placeholder="dd/mm/yyyy" required></input><br>
                        <label for="endDate">Please enter end date:</label>
                        <input type="date" id="endDate" name="endDate" placeholder="dd/mm/yyyy" required></input>
                        <br><button type='submit'>Submit</button>
                        </form>
                    """;
        } else if (context.method().equals("POST")) {
            String selectedMetric = context.formParam("metric");
            String selectedTime = context.formParam("time");
            String selectedStartDate = context.formParam("startDate");
            String selectedEndDate = context.formParam("endDate");
            String selectedFlag = context.formParam("flag");
            String metricName = selectedMetric;

            ArrayList<QUALITY> qualityList = getQuality(selectedMetric, selectedTime, selectedStartDate, selectedEndDate, selectedFlag);

            if (! qualityList.get(0).getName().equals("0")) {
                if (selectedMetric.equals("humid")) {
                metricName = "Humidity";
                } else if (selectedMetric.equals("okta")) {
                metricName = "Cloud Coverage";
                } else {
                String first = String.valueOf(metricName.charAt(0));
                metricName = metricName.replaceFirst(first,first.toUpperCase());
                }
                html += """
                        <h1>Results</h1>
                        <p>Displaying the first 
                        """ + qualityList.size() + " results.</p>" +
                        "<br><p><b>Selected Parameters</b><br>Flag: " + selectedFlag + "   Start Date: " + selectedStartDate +
                        "   End Date: " + selectedEndDate + "   Dataset: " + metricName + "</p>" +
                        """
                        <table class='descTables'><tr class='descTables'>
                        <th class='descTables'>LocationID</th>
                        <th class='descTables'>Site Name</th>
                        <th class='descTables'>Date</th>
                        <th class='descTables'>
                        """ + metricName +
                        """
                        </th></tr>         
                        """;

                for (QUALITY qualityObj : qualityList) {
                    String location = qualityObj.getLocation();
                    String name = qualityObj.getName();
                    String date = qualityObj.getDate();
                    String value = qualityObj.getMetricValue();

                    html += "<tr class='descTables'><td class='descTables'>" + location +
                            "</td><td class='descTables'>" + name +
                            "</td><td class='descTables'>" + date +
                            "</td><td class='descTables'>" + value +
                            "</td></tr>";
                            
                }
                html += "</table>";
            } else {
                html += "<p>No results</p>";
            }

            html += "<a href='/dataquality.html'>Back</a>";
        }
        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
                    <div class='footer'>
                        <p>COSC2803 - Studio Project Starter Code (ACC Apr2025)</p>
                    </div>
                """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    public ArrayList<QUALITY> getQuality(String metric, String time, String startDate, String endDate, String flag) {
        // Create the ArrayList of FlagQuality objects to return
        // Create an array called flags
        ArrayList<QUALITY> quality = new ArrayList<QUALITY>();
        String metricTime = metric;
        String metricTable = metric;

        if (metric.equals("humid") || metric.equals("okta")) {
            metricTime += time;
            if (metric.equals("humid")) {
                metricTable = "humidity";
            } else if (metric.equals("okta")) {
                metricTable = "cloud";
            }
        }

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // put in a timeout incase the db is not running
            statement.setQueryTimeout(30);

            // The SQL Query to be executed
            String query = "select location Location, name Name, YMD Date, " + metricTime +
                    " metricValue, " + metricTime + "qual metricQuality from " + metricTable +
                    " join location on " + metricTable + ".Location = location.site" +
                    " where metricQuality = '" + flag + "' and (Date between '"+ startDate +
                    "' and '" + endDate + "') order by Location asc, Date asc limit 50";
            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String location = results.getString("Location");
                String name = results.getString("Name");
                String date = results.getString("Date");
                String metricValue = results.getString("metricValue");

                // Create an FLAG Object
                QUALITY qualityObj = new QUALITY(location, name, date, metricValue);

                // Add the FLAG object to the flags array
                quality.add(qualityObj);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
            QUALITY qualityObj = new QUALITY("0","0","0","0");

            // Add the FLAG object to the flags array
            quality.add(qualityObj);
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

        return quality;
    }
}
