package app;

import java.util.ArrayList;
import java.text.NumberFormat;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageST3B implements Handler {
    public static final String URL = "/metricSimilarity.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "<html><head><meta charset='UTF-8'>"
        + "<title>Subtask B Level 3</title>";
        html += "<link rel='stylesheet' type='text/css' href='common.css' />";
        html += "<script>"
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
            + "</script></head><body>";

        html += PageIndex.navbar;

        html += "<div class='pageRef'><a href='/'>Home/</a>"
                + "<a href='metricSimilarity.html'>Metric Similarity</a></div>"

                + "<div><h1 class='level3Heading'>Compare Metric Similarity</h1></div>"

                + "<div class='content'>"

                + "<h2 class='form-heading2'>Explore and Find Metric Similarity across 2 Time periods</h2>"

                + "<form action='/metricSimilarity.html' method='POST'>"
                + "<div class='form-group'>"
                + "<label for='metric'>Select a metric:</label>"
                + "<select name='metric' id='metric' required>"
                + "<option value='Precipitation'>Precipitation</option>"
                + "<option value='Evaporation'>Evaporation</option>"
                + "<option value='MinTemp'>Temperature (Min)</option>"
                + "<option value='MaxTemp'>Temperature (Max)</option>"
                + "<option value='Humidity'>Humidity</option>"
                + "<option value='Sunshine'>Sunshine</option>"
                + "<option value='Cloud'>Cloud</option>"
                + "</select></div>"

                + "<div id='time-container' style='display:none;' class='form-group'>"
                + "<label for='time'>Select Observation Time:</label>"
                + "<select name='time' id='time'></select>"
                + "</div>"

                + "<div class='form-group'>"
                + "<label>Time Period 1:</label>"
                + "<input type='number' name='startDate1' placeholder='e.g. 2005' required> to "
                + "<input type='number' name='endDate1' placeholder='e.g. 2009' required>"
                + "</div>"

                + "<div class='form-group'>"
                + "<label>Time Period 2:</label>"
                + "<input type='number' name='startDate2' placeholder='e.g. 2010' required> to "
                + "<input type='number' name='endDate2' placeholder='e.g. 2015' required>"
                + "</div>"

                + "<div class='form-group'>"
                + "<label for='count'>Number of Similar Metrics:</label>"
                + "<input type='number' name='count' id='count' min='1' value='5' required>"
                + "</div>"

                + "<button class='submit-button' type='submit'>Analyse Metric</button>"
                + "</form>";

        if (context.method().equals("POST")) {
            String metric = context.formParam("metric");
            int startDate1 = Integer.parseInt(context.formParam("startDate1"));
            int endDate1 = Integer.parseInt(context.formParam("endDate1"));
            int startDate2 = Integer.parseInt(context.formParam("startDate2"));
            int endDate2 = Integer.parseInt(context.formParam("endDate2"));
            String time = context.formParam("time");
            int count = Integer.parseInt(context.formParam("count"));
            
            JDBCConnection jdbc = new JDBCConnection();
            ArrayList<SimilarMetric> results = jdbc.getSimilarMetrics(
                metric, startDate1, endDate1, startDate2, endDate2, time, count
            );
            
            if (!results.isEmpty()) {
                // Format numbers with commas
                NumberFormat nf = NumberFormat.getNumberInstance();
                
                html += "<h2 class='result-head2'>Similar Results for " + metric + ":</h2>"
                        + "<table class='team-Table'>"
                        + "<tr><th>Metric Name</th>"
                        + "<th>Total (" + startDate1 + "-" + endDate1 + ")</th>"
                        + "<th>Total (" + startDate2 + "-" + endDate2 + ")</th>"
                        + "<th>% Change</th>"
                        + "<th>Difference from " + metric + " (%)</th>"
                        + "</tr>";
                
                for (SimilarMetric data : results) {
                    String unit = jdbc.getUnit(data.getMetric());
                    String period1Value = nf.format(data.getPeriod1()) + " " + unit;
                    String period2Value = nf.format(data.getPeriod2()) + " " + unit;
                    String changeValue = String.format("%+.2f%%", data.getChange());
                    String diffValue = data.getMetric().equals(metric) ? 
                        "0.00%" : String.format("%+.2f%%", data.getDiff());
                    
                    html += String.format(
                        "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                        data.getMetric(),
                        period1Value.replace("&deg;", "°"),
                        period2Value.replace("&deg;", "°"),
                        changeValue,
                        diffValue
                    );
                }
                html += "</table>";
            } if (results.isEmpty()) {
            html += "<p>No similar metrics found for the selected time range or selected metric</p>";
            } else if (results.size() == 1) {
            html += "<p class='warning'>Only found data for the selected metric. "
                + "Other metrics might not have data for the selected time periods.</p>";
        }
        }

        html += "</div>"
                + "<div class='footer'>"
                + "<p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>"
                + "</div>"
                + "</body></html>";
        
        context.html(html);
    }
}