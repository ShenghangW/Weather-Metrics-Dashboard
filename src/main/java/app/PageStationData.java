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

        html += "<div class='content'><h1>Weather Stations</h1>";

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
                <form action="/searchResults.html" method="post">
                <label for="state">Select a state:</label>
                <select name="state" required>
                <option value="" disabled selected>Choose a state</option>
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
                ArrayList<Station> stationList = jdbc.getfilteredStations(state, startLat, endLat, metric, sortBy);
                
                    System.out.println("Form Parameters:");
    System.out.println("State: " + state);
    System.out.println("StartLat: " + startLat);
    System.out.println("EndLat: " + endLat);
    System.out.println("Metric: " + metric);
    System.out.println("SortBy: " + sortBy);


                if(!stationList.isEmpty()){
                     html += "<table class = 'team-table'>";
                           

                html += "<tr><th>Site</th><th>Name</th><th>State</th><th>Latitude</th><th>Longitude</th><th>" + metric + "</th></tr>";
                

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
            html += "<a href='/search.html'>Back</a>";
            }

            html += "</div><div class='footer'><p>Weather Report Project 2025</p></div></body></html>";
            context.html(html);
        }
        
}