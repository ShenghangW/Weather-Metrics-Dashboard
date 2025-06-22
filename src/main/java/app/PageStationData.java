package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageStationData implements Handler {
    public static final String URL = "/searchResults.html";

    @Override
    public void handle(Context context) {
        String html = "<html><head><title>Station Data</title>" +
                      "<link rel='stylesheet' href='common.css'>" +
                      "</head><body>";

        html += PageIndex.navbar;

        html += """
    <br>
   <div class='pageRef'>
        <a href='/'>Home/</a>
        <a href='StationSimilarity.html'>Similar Stations</a>
    </div> 
         """;
        html += "<div class='content'><h1>Weather Stations</h1>";
        html += """
            <h2>Welcome to the Weather Station Data Explorer</h2>
            <p>Use the form below to search for Australian weather stations based on region, latitude range, and specific environmental metrics.</p>
            <p>After submitting, you will not only see individual station records, but also a regional summary showing station counts and average values for your chosen metric.<p>
            <p>Whether you are interested in cloud coverage, humidity at different times of day, or long-term precipitation trends, this page helps you compare station-level data easily.</p>
            <p>All values are presented with clarity, and missing entries will be marked as <strong>'No data'</strong>.</p>
            """;

        html += """
                    <table class='reference-table'>
                        <tr><th>State</th><th>Latitude Range</th></tr>
                        <tr><td>AAT</td><td>-68.57 to -54.50</td></tr>
                        <tr><td>N.S.W.</td><td>-36.49 to -28.64</td></tr>
                        <tr><td>N.T.</td><td>-23.80 to -11.40</td></tr>
                        <tr><td>A.E.T.</td><td>-29.04 to -12.19</td></tr>
                        <tr><td>QLD</td><td>-28.62 to -12.79</td></tr>
                        <tr><td>S.A.</td><td>-37.75 to -27.56</td></tr>
                        <tr><td>TAS</td><td>-43.66 to -40.09</td></tr>
                        <tr><td>VIC</td><td>-39.13 to -34.24</td></tr>
                        <tr><td>W.A.</td><td>-34.37 to -13.75</td></tr>
                    </table>
                    <br>
                """;

        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<String> states = jdbc.getStates();
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
            html += """
                <form class = "form-group" action="/searchResults.html" method="post">
                <label for="state">Select a state:</label>
                <select name="state" required>
                <option value="" disabled selected>Choose a state</option><br><br>
                """;
            for (String state : states) {
                html += "<option value='" + state + "'>" + state + "</option>";
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
                        </select><br><br>
                        <div id="time-container" style="display:none;">
                        <label for="time">Select time:</label>
                        <select name="time" id="time"></select><br><br>
                        </div>
                        <label for="startLat">Starting Latitude:</label>
                        <input type="number" name="startLat" step="0.01" required><br><br> 
                        <label for="endLat">Ending Latitude:</label>
                        <input type="number" name="endLat" step="0.01" required><br><br>
                        <label for="sortBy">Sort the data by:</label>
                        <select name="sortBy" required>
                        <option value="site">Site Name</option>
                        <option value="region">Region</option>
                        <option value="latitude">Latitude</option>
                        </select><br><br>
                        <input type="submit" value="Search">
                        </form>
                """;
                if (context.method().equals("POST")) {
                String state = context.formParam("state");
                double startLat = Double.parseDouble(context.formParam("startLat"));
                double endLat = Double.parseDouble(context.formParam("endLat"));
                String metric = context.formParam("metric");
                String sortBy = context.formParam("sortBy");
                String time = context.formParam("time");
                ArrayList<Station> stationList = jdbc.getfilteredStations(state, startLat, endLat, metric, sortBy, time);
                
                String displayName = switch (metric.toLowerCase()) {
                    case "maxtemp" -> "Maximum Temperature";
                    case "mintemp" -> "Minimum Temperature";
                    case "evaporation" -> "Evaporation";
                    case "precipitation" -> "Precipitation";
                    case "sunshine" -> "Sunshine";
                    case "humid" -> "Humidity";
                    case "okta" -> "Cloud Coverage";
                    default -> metric;
                };

                if(!stationList.isEmpty()){
                     html += "<table class = 'team-table'>";
                           

                html += "<tr><th>Station ID</th><th>Name</th><th>State</th><th>Latitude</th><th>Longitude</th><th>" + displayName + "</th></tr>";
                

                for(Station s : stationList){
                    html += "<tr>" +
                        "<td>" + s.getSite() + "</td>" +
                        "<td>" + s.getName() + "</td>" +
                        "<td>" + s.getState() + "</td>" +
                        "<td>" + s.getLat() + "</td>" +
                        "<td>" + s.getlongt() + "</td>" +
                        "<td>" + s.getmetric() + "</td>" +
                        "</tr>";
                    }
                }
                else{
                   html += "<p>No Results found.</p>";
                }

                 html += "</table>";
               

                ArrayList<RegionSummary> regionSummaries = jdbc.getRegionalSummary(state, startLat, endLat, metric, time);

                html += "<h3>Summary by Region</h3>";
                if (!regionSummaries.isEmpty()) {
                    html += "<table class='team-table'>";
                    html += "<tr><th>Region</th><th>Number of Stations</th><th>Average " + displayName + "</th></tr>";
                
                    for (RegionSummary rsum : regionSummaries) {
                        html += "<tr>" +
                                "<td>" + rsum.getRegionName() + "</td>" +
                                "<td>" + rsum.getStationCount() + "</td>" +
                                "<td>" + String.format("%.2f", rsum.getAverageMetric()) + "</td>" +
                                "</tr>";
                    }

                html += "</table>";
            } else {
                html += "<p>No regional summary available for these conditions.</p>";
            }
            html += "<a href='/search.html'>Back</a>";
            }

            html += "</div><div class='footer'><p>Weather Report Project 2025</p></div></body></html>";
            context.html(html);
        }
}