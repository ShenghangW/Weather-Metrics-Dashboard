package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageSearchMetrics implements Handler {
    public static final String URL = "/metricExplorer.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "<html><head><title>Metric Explorer</title>"
                + "<link rel='stylesheet' type='text/css' href='common.css' />"

                + "<script>"
                + "function updateTimeOptions() {"
                + "  const metric = document.getElementById('metric').value;"
                + "  const timeContainer = document.getElementById('time-container');"
                + "  const timeSelect = document.getElementById('time');"
                + "  timeSelect.innerHTML = '';"
                + "  if (metric === 'Humidity' || metric === 'Cloud') {"
                + "    const times = ['00','03','06','09','12','15','18','21'];"
                + "    times.forEach(time => {"
                + "      const option = document.createElement('option');"
                + "      option.value = time;"
                + "      option.text = time + ':00';"
                + "      timeSelect.appendChild(option);"
                + "    });"
                + "    timeContainer.style.display = 'block';"
                + "    timeSelect.required = true;"
                + "  } else {"
                + "    timeContainer.style.display = 'none';"
                + "    timeSelect.required = false;"
                + "  }"
                + "}"
                + "document.addEventListener('DOMContentLoaded', function() {"
                + "  document.getElementById('metric').addEventListener('change', updateTimeOptions);"
                + "  updateTimeOptions();"
                + "});"
                + "</script>"

                + "</head><body>"

                + PageIndex.navbar

                + "<br>"
                + "<div class='pageRef'>"
                    + "<a href='/'>Home/</a>"
                    + "<a href='metricExplorer.html'>Metric Explorer</a>"
                + "</div>"

                + "<div><h1 class='metricExplorerHeader'>Metric Explorer</h1></div>"
                
                + "<div class='content'>"
                + "<h2 class='form-heading2'>Explore a Metric Across Various Stations</h2>"

                + "<form action='/metricExplorer.html' method='POST'>"
                +   "<div class='form-group'>"
                +     "<label for='metric'>Select a Climate Metric:</label>"
                +     "<select name='metric' id='metric' required>"
                +       "<option value='Precipitation'>Precipitation</option>"
                +       "<option value='Evaporation'>Evaporation</option>"
                +       "<option value='MinTemp'>Temperature (Min)</option>"
                +       "<option value='MaxTemp'>Temperature (Max)</option>"
                +       "<option value='Humidity'>Humidity</option>"
                +       "<option value='Sunshine'>Sunshine</option>"
                +       "<option value='Cloud'>Cloud Coverage</option>"
                +     "</select>"
                +   "</div>"

                +   "<div class='form-group'>"
                +     "<label for='startStation'>Select Station ID From:</label>"
                +     "<input type='number' name='startStation' id='startStation' placeholder='Select from 1006' required>"
                +     "<label for='endStation'> to </label>"
                +     "<input type='number' name='endStation' id='endStation' placeholder='Select up to 300004' required>"
                +   "</div>"

                +   "<div class='form-group'>"
                +     "<label>Date Range</label>"
                +     "<input type='date' name='startDate' required> to "
                +     "<input type='date' name='endDate' required>"
                +   "</div>"

                +   "<div id='time-container' style='display:none;' class='form-group'>"
                +     "<label for='time'>Select Observation Time:</label>"
                +     "<select name='time' id='time'></select>"
                +   "</div>"

                +   "<div class='form-group'>"
                +     "<label for='order'>Order by</label>"
                +     "<select name='order' id='order' required>"
                +       "<option value='Asc'>Ascending</option>"
                +       "<option value='Desc'>Descending</option>"
                +     "</select>"
                +   "</div>"
                + "<br>"
                +   "<button class='submit-button' type='submit'>Analyse Metric</button>"
                + "</form>";

        if (context.method().equals("POST")) {
            String metric = context.formParam("metric");
            String time = context.formParam("time");
            String startStation = context.formParam("startStation");
            String endStation = context.formParam("endStation");
            String startDate = context.formParam("startDate");
            String endDate = context.formParam("endDate");
            String order = context.formParam("order");

            

            JDBCConnection jdbc = new JDBCConnection();
            ArrayList<MetricData> data = jdbc.getMetricData(metric, startStation, endStation, startDate, endDate, order, time);

            if(!data.isEmpty()) {
                html += "<h2 class='result-head2'>Results for " + metric + ":</h2>"
                        +"<h3 class='result-subhead'>NOTE: The Total Figure of " + metric + " in ALL States based on the Station ID's Selected is Locat ed at the Bottom of the Page!</h3>"
                        + "<table class='team-Table'><tr>"
                        + "<th>Station ID</th><th>Date</th><th>" + metric + "</th>"
                        + "</tr>";

                for(MetricData entry : data) {
                    html += String.format(
                        "<tr><td>%s</td><td>%s</td><td>%s</td></tr>",
                        entry.getLocation(), entry.getDate(), entry.getMetricValue()
                    );
                }
                html += "</table>";

                ArrayList<StateSummary> stateResults = jdbc.getStateSummary(metric, startStation, endStation, startDate, endDate, order, time);
                if (!stateResults.isEmpty()) {
                    html += "<h2 class='head2'>Result summary in States</h2>"
                        + "<table class='team-Table'><tr>"
                        + "<th>State</th><th>Total " + metric + "</th>"
                        + "</tr>";

                    for(StateSummary entry : stateResults) {
                        html += String.format(
                            "<tr><td>%s</td><td>%.2f</td></tr>",
                            entry.getState(), entry.getTotalValue()
                        );
                    }
                    html += "</table>";
                }
            } else {
                html += "<p>No data found for selected Metric</p>";
            }
        }

        html += "</div>"
            + "<div class='footer'>"
            + "<p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>"
            + "</div></body></html>";

        context.html(html);
    }
}