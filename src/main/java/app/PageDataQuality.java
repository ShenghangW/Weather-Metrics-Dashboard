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

    public static String dropScript = """
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

        public static String metricDrop = """
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
                    </div>
                """;

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

        html += dropScript;
        html += """
                    <p>To take a closer look at the poor quality data, please select the following options:</p>
                    <form class =''action="/dataquality.html" method="post">
                    <div class='form-group'>
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

        html += "</select></div><div class='form-group'>";

        html += metricDrop;

        html += """ 
                    </div>
                    <br>
                    <div class='form-group'>
                    <label for="startDate">Start date:</label>
                    <input type="date" id="startDate" name="startDate" placeholder="dd/mm/yyyy" required></input><br>
                    </div>
                    <div class='form-group'>
                    <label for="endDate">End date:</label>
                    <input type="date" id="endDate" name="endDate" placeholder="dd/mm/yyyy" required></input><br>
                    </div>
                    <div class='form-group'>
                    <label for="sort">Sort by:</label>
                    <select name="sort" id="sort">
                    <option value="location">Location ID</option>
                    <option value="name">Site Name</option>
                    <option value="date">Date</option>
                    <option value="measure">Measurement</option>
                    </select>
                    </div>
                    <div class='form-group'>
                    <label for="ascdesc"></label><select name="ascdesc" id="ascdesc">
                    <option value="asc">Ascending</option>
                    <option value="desc">Descending</option>
                    </select>
                    </div>
                    <p><b>State Summary</b></p>
                    <div class='form-group'>
                    <label for="state">Select State:</label>
                    <select name="state" id="state">
                    <option value="A.A.T">Antarctic Territories</option>
                    <option value="A.E.T.">Extended Territories</option>
                    <option value="N.S.S.">New South Wales</option>
                    <option value="N.T.">Northern Territory</option>
                    <option value="QLD">Queensland</option>
                    <option value="S.A.">South Australia</option>
                    <option value="TAS">Tasmania</option>
                    <option value="VIC">Victoria</option>
                    <option value="W.A.">Western Australia</option>
                    </select>
                    </div>
                    <br><button class = 'submit-button' type='submit'>Submit</button>
                    </form>
                """;

        if (context.method().equals("POST")) {
            String selectedMetric = context.formParam("metric");
            String selectedTime = context.formParam("time");
            String selectedStartDate = context.formParam("startDate");
            String selectedEndDate = context.formParam("endDate");
            String selectedFlag = context.formParam("flag");
            String selectedSort = context.formParam("sort");
            String selectedAscDesc = context.formParam("ascdesc");
            String selectedState = context.formParam("state");
            String metricName = selectedMetric;

            ArrayList<QUALITY> qualityList = getQuality(selectedMetric, selectedTime, selectedStartDate,
                    selectedEndDate, selectedFlag, selectedSort, selectedAscDesc);
            ArrayList<SUMMARY> summaryList = getSummary(selectedMetric, selectedTime, selectedStartDate,
                    selectedEndDate, selectedState);

            if (qualityList.size() > 0) {
                if (selectedMetric.equals("humid")) {
                    metricName = "Humidity";
                } else if (selectedMetric.equals("okta")) {
                    metricName = "Cloud Coverage";
                } else {
                    String first = String.valueOf(metricName.charAt(0));
                    metricName = metricName.replaceFirst(first, first.toUpperCase());
                }
                html += """
                        <h1>Results</h1>
                        <p>Displaying the first
                        """ + qualityList.size() + " results.</p>" +
                        "<p><table class='selected-params'><tr class='selected-params'><td class='selected-params' colspan=4 align='center'><b>Selected Parameters</b></td></tr><br>"
                        +
                        "<tr class='selected-params'><td class='selected-params'>" +
                        "<b>Flag:</b> " + selectedFlag + "</td><td class='selected-params'><b>Start Date:</b> "
                        + selectedStartDate +
                        "</td><td class='selected-params'><b>End Date:</b> " + selectedEndDate
                        + "</td><td class='selected-params'><b> Dataset:</b> " + metricName +
                        "</td></tr></table></p>" +
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
                html += "</table><br>";

                html += "<h1>Summary</h1>" +
                        "<p>Number of each quality flag for " + metricName + " data in " + selectedState +
                        " from " + selectedStartDate + " to " + selectedEndDate + ".";

                html += """
                        <table class='descTables'><tr class='descTables'>
                        <th class='descTables'>Flag Name</th>
                        <th class='descTables'>Number of Flags</th></tr>
                        """;

                for (SUMMARY summaryObj : summaryList) {
                    String flagName = summaryObj.getFlagName();
                    String flagNumber = summaryObj.getFlagNumber();

                    html += "<tr class='descTables'><td class='descTables'>" + flagName +
                            "</td><td class='descTables'>" + flagNumber + "</td></tr>";
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
                        <p>Weather Report(2025)</p>
                    </div>
                """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    public ArrayList<QUALITY> getQuality(String metric, String time, String startDate, String endDate, String flag,
            String sort, String ascdesc) {
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

        if (sort.equals("metric")) {
            sort = metricTable;
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
                    " where metricQuality = '" + flag + "' and (Date between '" + startDate +
                    "' and '" + endDate + "') order by " + sort + " " + ascdesc + " limit 50";
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
            QUALITY qualityObj = new QUALITY("0", "0", "0", "0");

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

    public ArrayList<SUMMARY> getSummary(String metric, String time, String startDate, String endDate, String state) {
        // Create the ArrayList of FlagQuality objects to return
        // Create an array called flags
        ArrayList<SUMMARY> summary = new ArrayList<SUMMARY>();
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
            String query = "select location l, state s, ymd date, " + metricTime + "qual FlagName, count(" + metricTime
                    +
                    "qual) NumberOfFlags from " + metricTable + " join location on " + metricTable
                    + ".Location == location.site where state == '" +
                    state + "' and (date between '" + startDate + "' and '" + endDate
                    + "') and FlagName != ' ' group by FlagName order by FlagName";

            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String flagName = results.getString("FlagName");
                String flagNumber = results.getString("NumberOfFlags");

                // Create an FLAG Object
                SUMMARY summaryObj = new SUMMARY(flagName, flagNumber);

                // Add the FLAG object to the flags array
                summary.add(summaryObj);
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

        return summary;
    }
}
