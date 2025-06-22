package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageStationData implements Handler {
    public static final String URL = "/searchResults.html";

    @Override
    public void handle(Context context) throws Exception {
        JDBCConnection jdbc = new JDBCConnection();
        String html = "";

        html += """
        <html>
        <head>
            <title>Station Data</title>
            <link rel='stylesheet' type='text/css' href='common.css' />
            <script>
                function updateOptions() {
                    const metric = document.getElementById('metric').value;
                    const time = document.getElementById('time');
                    time.innerHTML = '';

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
                        document.getElementById('time-group').style.display = 'block';
                    } else {
                        document.getElementById('time-group').style.display = 'none';
                    }
                }
            </script>
        </head>
        <body>
        """;

        html += PageIndex.navbar;

        html += """
        <br>
        <div class='pageRef'>
            <a href='/'>Home/</a>
            <a href='searchResults.html'>Similar Stations</a>
        </div>

        <div class='stationdataheader'><h1>Weather Station Data Explorer</h1></div>

        <div class='content'>
            <h2 class='form-heading2'>Search for Australian Weather Stations</h2>
            <p>Use the form below to filter stations based on region, latitude range, and environmental metrics.</p>
            <p>Results will include individual station records and a regional summary with station counts and average values.</p>
            <p>Missing entries will be marked as <strong>'No data'</strong>.</p>

            <form action='/searchResults.html' method='POST'>
                <div class='form-group'>
                    <label for='state'>Select a state:</label>
                    <select name='state' required>
                        <option value='' disabled selected>Choose a state</option>
        """;

        ArrayList<STATE> states = jdbc.getStates();
        for (STATE stateInfo : states) {
            html += "<option value='" + stateInfo.getState() + "'>" + stateInfo.getStateName() + "</option>";
        }

        html += """
                    </select>
                </div>

                <div class='form-group'>
                    <label for='metric'>Select dataset:</label>
                    <select name='metric' id='metric' onchange='updateOptions()' required>
                        <option value='' disabled selected>Select dataset</option>
                        <option value='precipitation'>Precipitation</option>
                        <option value='evaporation'>Evaporation</option>
                        <option value='maxtemp'>Temperature (Max)</option>
                        <option value='mintemp'>Temperature (Min)</option>
                        <option value='humid'>Humidity</option>
                        <option value='sunshine'>Sunshine</option>
                        <option value='okta'>Cloud Coverage</option>
                    </select>
                </div>

                <div class='form-group' id='time-group' style='display: none;'>
                    <label for='time'>Select time:</label>
                    <select name='time' id='time'></select>
                </div>

                <div class='form-group'>
                    <label for='startLat'>Starting Latitude:</label>
                    <input type='number' name='startLat' step='0.01' required>
                </div>

                <div class='form-group'>
                    <label for='endLat'>Ending Latitude:</label>
                    <input type='number' name='endLat' step='0.01' required>
                </div>

                <div class='form-group'>
                    <label for='sortBy'>Sort the data by:</label>
                    <select name='sortBy' required>
                        <option value='site'>Site Name</option>
                        <option value='region'>Region</option>
                        <option value='latitude'>Latitude</option>
                    </select>
                </div>

                <button type='submit'>Search</button>
            </form>
        </div>
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

            if (!stationList.isEmpty()) {
                html += """
                    <h2 class='result-head2'>Weather Station Data:</h2>
                    <div class='table-container'>
                    <table class='team-Table'>
                        <thead>
                            <tr>
                                <th>Station ID</th>
                                <th>Name</th>
                                <th>State</th>
                                <th>Latitude</th>
                                <th>Longitude</th>
                    """;

                html += "<th>" + displayName + "</th>";

                html += """
                        </tr>
                        </thead>
                        <tbody>
                        """;

                for (Station s : stationList) {
                    html += String.format("""
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%.2f</td>
                        <td>%.2f</td>
                        <td>%s</td>
                    </tr>
                    """, s.getSite(), s.getName(), s.getState(), s.getLat(), s.getlongt(), s.getmetric());
                }

                html += """
                    </tbody>
                </table>
                </div>
                """;
            } else {
                html += "<p>No Results found.</p>";
            }

            ArrayList<RegionSummary> regionSummaries = jdbc.getRegionalSummary(state, startLat, endLat, metric, time);
            html += "<h3>Summary by Region</h3>";

            if (!regionSummaries.isEmpty()) {
                html += """
                        <div class='table-container'>
                        <table class='team-Table'>
                            <thead>
                                <tr>
                                    <th>Region</th>
                                    <th>Number of Stations</th>
                            """;
                                
                html += "<th>Average " + displayName + "</th>"; 
                                
                html += """
                                </tr>
                            </thead>
                            <tbody>
                        """;


                for (RegionSummary rsum : regionSummaries) {
                    html += String.format("""
                    <tr>
                        <td>%s</td>
                        <td>%d</td>
                        <td>%.2f</td>
                    </tr>
                    """, rsum.getRegionName(), rsum.getStationCount(), rsum.getAverageMetric());
                }

                html += """
                    </tbody>
                </table>
                </div>
                """;
            } else {
                html += "<p>No regional summary available.</p>";
            }
        }

        html += "</body></html>";
        context.html(html);
    }
}