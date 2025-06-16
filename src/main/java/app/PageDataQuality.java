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
        // This uses a Java v15+ Text Block
        html = html + """
                     <div class='topnav'>
                        <a href='/'>Homepage</a>
                        <a href='mission.html'>Our Mission</a>
                  <a href="equipment.html">Climate Equipment</a>
                        <a href='page2A.html'>Sub Task 2.A</a>
                        <a href='page2B.html'>Sub Task 2.B</a>
                        <a href='dataquality.html'>Data Quality</a>
                        <a href='page3A.html'>Sub Task 3.A</a>
                        <a href='page3B.html'>Sub Task 3.B</a>
                        <a href='page3C.html'>Sub Task 3.C</a>
                    </div>
                """;

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
                        const category = document.getElementById('category').value;
                        const metric = document.getElementById('metric');
                        metric.innerHTML = ''; // Clear previous options

                        const options = {
                            hum: ['12am', '3am', '6am', '9am', '12pm', '3pm', '6pm', '9pm'],
                            cloud: ['12am', '3am', '6am', '9am', '12pm', '3pm', '6pm', '9pm']
                        };

                        if (category && options[category]) {
                            options[category].forEach(function(item) {
                                const option = document.createElement('option');
                                option.text = item;
                                metric.appendChild(option);
                            });
                            document.getElementById('metric-container').style.display = 'block';
                        } else {
                            document.getElementById('metric-container').style.display = 'none';
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
                            html += "<option value='" + flagName.toLowerCase() + "'>" + flagName + "</option>";
                        }
                    }

            html += """
                        </select>
                        <label for="category"></label>
                        <select name="category" id="category" onchange="updateOptions()" required>
                        <option value="" disabled selected>Select dataset</option>
                        <option value="precip">Precipitation</option>
                        <option value="evap">Evaporation</option>
                        <option value="maxtemp">Temperature (Max)</option>
                        <option value="mintemp">Temperature (Min)</option>
                        <option value="hum">Humidity</option>
                        <option value="sun">Sunshine</option>
                        <option value="cloud">Cloud Coverage</option>
                        </select>
                        <div id='metric-container' style='display:none; margin-top:10px;'>
                        <label for='metric'>Select time of day:</label>
                        <select name='metric' id='metric'></select>
                        </div><br></br>
                        <label for="startDate">Please enter start date:</label>
                        <input type="date" id="startDate" name="startDate" placeholder="dd/mm/yyyy" required></input><br></br>
                        <label for="endDate">Please enter end date:</label>
                        <input type="date" id="endDate" name="endDate" placeholder="dd/mm/yyyy" required></input>
                        <br></br><button type='submit'>Submit</button>
                        </form>
                    """;
        } else if (context.method().equals("POST")) {
            String selectedCategory = context.formParam("category");
            String selectedMetric = context.formParam("metric");
            String selectedStartDate = context.formParam("startDate");
            String selectedEndDate = context.formParam("endDate");

            html += "<p>You selected dataset: <strong>" + selectedCategory + selectedMetric + 
                    selectedStartDate + selectedEndDate +
                    "</strong></p>" +
                    "<a href='/dataquality.html'>Back</a>";
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

}
