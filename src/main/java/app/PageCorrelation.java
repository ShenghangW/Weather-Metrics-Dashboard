package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageCorrelation implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/correlation.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" +
                "<title>Data Correlation</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        html = html + PageIndex.navbar;

        // Add header content block
        html = html + """
                    <div class='correlationHeader'>
                        <h1>Data Correlation</h1>
                    </div>
                """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html
                + """
                        <p>This page can be used to investigate correlation between weather metrics. Please select a reference weather
                        station and the metric of interest. Results can be grouped over various time periods.</p>
                        """;

        html += PageDataQuality.dropScript;

        JDBCConnection jdbc = new JDBCConnection();

        html += "<form action='/correlation.html' method='post' class='form-group'>";

        html += jdbc.stationDrop();

        html += "<br>" + PageDataQuality.metricDrop;

        html += """
                <label for='period'>Group data by: </label>
                <select name='period' id='period' required>
                <option value='' disabled selected>Time period</option>
                <option value='Week'>1 Week</option>
                <option value='Month'>1 Month</option>
                <option value='Quarter'>3 Months</option>
                <option value='Half'>6 Months</option>
                <option value='Year'>1 Year</option>
                <option value='HalfDecade'>5 Years</option>
                <option value='Decade'>10 years</option>
                </select>
                </div>
                <br>
                <div class='form-group'>
                <label for="startDate">Start date:</label>
                <input type="date" id="startDate" name="startDate" placeholder="dd/mm/yyyy" required></input><br>
                </div>
                <br><button class = 'submit-button' type='submit'>Submit</button>
                </form>
                """;

        if (context.method().equals("POST")) {
            String selectedStation = context.formParam("sites");
            String selectedMetric = context.formParam("metric");
            String selectedTime = context.formParam("time");
            String selectedPeriod = context.formParam("period");
            String selectedStartDate = context.formParam("startDate");

            ArrayList<COMPARE> dataList = compareData(selectedStation,selectedMetric,selectedTime,selectedPeriod,selectedStartDate);

            html += """
                    <br><br>
                    <table class='team-table'><tr>
                    <th>Metric Name</th>
                    <th>Average Period 1</th>
                    <th>Average Period 2</th>
                    <th>Change (%)</th>
                    <th>Metric Trend (Correlation)</th></tr>
                    """;

            for (COMPARE compareObj : dataList) {
                int measurementID = compareObj.getMeasurementID()/10000000;
                double value1 = compareObj.getValue1();
                double value2 = compareObj.getValue2();
                double delta = compareObj.getDelta();
                String metricName = "";
                String correlation = "";

                switch (measurementID) {
                    case 1:
                        metricName = "Precipitation";
                        break;
                    case 2:
                        metricName = "Evaporation";
                        break;
                    case 3:
                        metricName = "Temperature";
                        break;
                    case 4:
                        metricName = "Humidity";
                        break;
                    case 5:
                        metricName = "Sunshine";
                        break;
                    case 6:
                        metricName = "Cloud Coverage";
                        break;
                }

                if (delta < 1) {
                    correlation = "Negative";
                } else if (delta > 1) {
                    correlation = "Positive";
                } else {
                    correlation = "Neutral";
                }

                html += "<tr><td>" + metricName + "</td>" +
                        "<td>" + Math.round(value1 * 100.00)/100.00 + "</td>" +
                        "<td>" + Math.round(value2 * 100.00)/100.00 + "</td>" +
                        "<td>" + Math.round(delta * 100.00)/100.00 + "</td>" +
                        "<td>" + correlation + "</td></tr>";
            }

            html += "</table>";

        }

        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
                    <div class='footer'>
                        <p>Weather Report (2025)</p>
                    </div>
                """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    public static ArrayList<SITE> getSite() {

        ArrayList<SITE> sites = new ArrayList<SITE>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "select site, name, state from location order by state";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String site = results.getString("site");
                String siteNameUpper = results.getString("name");
                String state = results.getString("state");

                String siteNameLower = siteNameUpper.toLowerCase();
                String siteName = "";
                siteName += String.valueOf(siteNameLower.charAt(0)).toUpperCase();

                for (int i = 1; i < siteNameLower.length(); ++i) {
                    siteName += String.valueOf(siteNameLower.charAt(i));
                    if (((siteNameLower.charAt(i) == ' ') && (siteNameLower.charAt(i + 1) != '('))
                            || (siteNameLower.charAt(i) == '(')) {
                        siteName += String.valueOf(siteNameLower.charAt(i + 1)).toUpperCase();
                        ++i;
                    }
                }

                SITE sitesObj = new SITE(site, siteName, state);

                sites.add(sitesObj);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return sites;
    }

    public ArrayList<COMPARE> compareData(String site, String metric, String time, String period, String startDate) {

        ArrayList<COMPARE> data = new ArrayList<COMPARE>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "select h1.MeasurementId m1, h1.Location l1, h1.ymd ymd1, h2.ymd ymd2, avg(h1.humid" + time
                    + ") v1, avg(h2.humid" + time + ") v2, (((avg(h2.humid" + time + ")-avg(h1.humid" + time
                    + "))/avg(h1.humid" + time
                    + "))*100) delta from humidity h1 join humidity h2 on h1.Location == h2.Location where (ymd1 between '"
                    + startDate + "' and '2010-01-01') and (ymd2 between '2010-01-01' and '2021-01-01') and (l1 == "
                    + site + ")" +
                    " union " +
                    "select h1.MeasurementId m1, h1.Location l1, h1.ymd ymd1, h2.ymd ymd2, avg(h1.precipitation) v1, avg(h2.precipitation) v2, (((avg(h2.precipitation)-avg(h1.precipitation))/avg(h1.precipitation))*100) delta from precipitation h1 join precipitation h2 on h1.Location == h2.Location where (ymd1 between '"
                    + startDate + "' and '2010-01-01') and (ymd2 between '2010-01-01' and '2021-01-01') and (l1 == "
                    + site + ")" +
                    " union " +
                    "select h1.MeasurementId m1, h1.Location l1, h1.ymd ymd1, h2.ymd ymd2, avg(h1.evaporation) v1, avg(h2.evaporation) v2, (((avg(h2.evaporation)-avg(h1.evaporation))/avg(h1.evaporation))*100) delta from evaporation h1 join evaporation h2 on h1.Location == h2.Location where (ymd1 between '"
                    + startDate + "' and '2010-01-01') and (ymd2 between '2010-01-01' and '2021-01-01') and (l1 == "
                    + site + ")" +
                    " union " +
                    "select h1.MeasurementId m1, h1.Location l1, h1.ymd ymd1, h2.ymd ymd2, avg(h1.maxtemp) v1, avg(h2.maxtemp) v2, (((avg(h2.maxtemp)-avg(h1.maxtemp))/avg(h1.maxtemp))*100) delta from temperature h1 join temperature h2 on h1.Location == h2.Location where (ymd1 between '"
                    + startDate + "' and '2010-01-01') and (ymd2 between '2010-01-01' and '2021-01-01') and (l1 == "
                    + site + ")" +
                    " union " +
                    "select h1.MeasurementId m1, h1.Location l1, h1.ymd ymd1, h2.ymd ymd2, avg(h1.okta" + time
                    + ") v1, avg(h2.okta" + time + ") v2, (((avg(h2.okta" + time + ")-avg(h1.okta" + time
                    + "))/avg(h1.okta" + time
                    + "))*100) delta from cloud h1 join cloud h2 on h1.Location == h2.Location where (ymd1 between '"
                    + startDate + "' and '2010-01-01') and (ymd2 between '2010-01-01' and '2021-01-01') and (l1 == "
                    + site + ")" +
                    " union " +
                    "select h1.MeasurementId m1, h1.Location l1, h1.ymd ymd1, h2.ymd ymd2, avg(h1.sunshine) v1, avg(h2.sunshine) v2, (((avg(h2.sunshine)-avg(h1.sunshine))/avg(h1.sunshine))*100) delta from sunshine h1 join sunshine h2 on h1.Location == h2.Location where (ymd1 between '"
                    + startDate + "' and '2010-01-01') and (ymd2 between '2010-01-01' and '2021-01-01') and (l1 == "
                    + site + ")" +
                    "order by delta desc";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                int measurementID = results.getInt("m1");
                double value1 = results.getDouble("v1");
                double value2 = results.getDouble("v2");
                double delta = results.getDouble("delta");

                COMPARE compareObj = new COMPARE(measurementID, value1, value2, delta);

                data.add(compareObj);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return data;
    }

}
