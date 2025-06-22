package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageStationSimilarity implements Handler {
    public static final String URL = "/StationSimilarity.html";

@Override
public void handle(Context context) throws Exception {
    JDBCConnection jdbc = new JDBCConnection();

    String html = "";
html += """
    <html>
    <head>
        <title>Station Similarity</title>
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
        <a href='StationSimilarity.html'>Similar Stations</a>
    </div> 

    <div><h1 class='similarstationheader'>Similar Station Finder</h1></div>

    <div class='content'>
        <h2 class='form-heading2'>Find Similar Stations Based on Climate Metric Changes</h2>

        <form action='/StationSimilarity.html' method='POST'>
            <div class='form-group'>
                <label for='refStation'>Reference Station ID:</label>
                <input type='number' name='refStation' id='refStation' placeholder='e.g. 86071' required>
            </div>

            <div class='form-group'>
                <label for='metric'>Select a Climate Metric:</label>
                <select name='metric' id='metric' onchange='updateOptions()' required>
                    <option value='avgTemp'>Average Temperature</option>
                    <option value='minTemp'>Min Temperature</option>
                    <option value='maxTemp'>Max Temperature</option>
                    <option value='evaporation'>Evaporation</option>
                    <option value='precipitation'>Precipitation</option>
                    <option value='sunshine'>Sunshine</option>
                    <option value='humid'>Humidity</option>
                    <option value='okta'>Cloud Coverage</option>
                </select>
            </div>

            <div class='form-group' id='time-group' style='display: none;'>
                <label for='time'>Select Time:</label>
                <select name='time' id='time'>
                    <option value='00'>12am</option>
                    <option value='03'>3am</option>
                    <option value='06'>6am</option>
                    <option value='09'>9am</option>
                    <option value='12'>12pm</option>
                    <option value='15'>3pm</option>
                    <option value='18'>6pm</option>
                    <option value='21'>9pm</option>
                </select>
            </div>

            <div class='form-group'>
                <label>Time Period 1:</label>
                <input type='number' name='start1' placeholder='e.g. 2005' required> to
                <input type='number' name='end1' placeholder='e.g. 2009' required>
            </div>

            <div class='form-group'>
                <label>Time Period 2:</label>
                <input type='number' name='start2' placeholder='e.g. 2010' required> to
                <input type='number' name='end2' placeholder='e.g. 2015' required>
            </div>

            <div class='form-group'>
                <label for='count'>Number of Similar Stations:</label>
                <input type='number' name='count' id='count' min='1' value='5' required>
            </div>

            <button type='submit'>Find Similar Stations</button>
        </form>
    </div>
""";

    if (context.method().equals("POST")) {
        String refStation = context.formParam("refStation");
        String metric = context.formParam("metric");
        int start1 = Integer.parseInt(context.formParam("start1"));
        int end1 = Integer.parseInt(context.formParam("end1"));
        int start2 = Integer.parseInt(context.formParam("start2"));
        int end2 = Integer.parseInt(context.formParam("end2"));
        int count = Integer.parseInt(context.formParam("count"));
        String time = "";

        if (metric.equalsIgnoreCase("humid") || metric.equalsIgnoreCase("okta")) {
            time = context.formParam("time");
        }

        ArrayList<SimilarStation> results = jdbc.getSimilarStations(refStation, metric, start1, end1, start2, end2, count, time);

        if (!results.isEmpty()) {
    html += """
        <h2 class='result-head2'>Most Similar Stations:</h2>
        <div class='table-container'>
        <table class='team-Table'>
            <thead>
                <tr>
                    <th>Station ID</th>
                    <th>Site Name</th>
                    <th>Period 1 Avg</th>
                    <th>Period 2 Avg</th>
                    <th>% Change</th>
                    <th>Similarity Score</th>
                </tr>
            </thead>
            <tbody>
    """;

    for (SimilarStation s : results) {
        html += String.format("""
            <tr>
                <td>%s</td>
                <td>%s</td>
                <td>%.2f</td>
                <td>%.2f</td>
                <td>%.2f%%</td>
                <td>%.2f%%</td>
            </tr>
        """, s.getSite(), s.getName(), s.getPeriod1Avg(), s.getPeriod2Avg(), s.getPercentChange(), s.getSimilarityScore());
    }

    html += """
            </tbody>
        </table>
        </div>
    """;
} else {
    html += "<p>No similar stations found for the selected configuration.</p>";
}

    html += """
        </div>
        <div class='footer'>
            <p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>
        </div>
        </body>
        </html>
    """;

}

context.html(html);
}
}