package app;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

/**
 * Class for Managing the JDBC Connection to a SQLLite Database.
 * Allows SQL queries to be used with the SQLLite Databse in Java.
 *
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Halil Ali, 2024. email: halil.ali@rmit.edu.au
 * @editor David Eccles, 2025 email: david.eccles@rmit.edu
 */

public class JDBCConnection {

    // Name of database file (contained in database folder)
    public static final String DATABASE = "jdbc:sqlite:database/climate.db";

    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCConnection() {
        System.out.println("Created JDBC Connection Object");
    }

    /**
     * Get all of the Flag Descriptions
     */
    public ArrayList<FLAG> getFlags() {
        // Create the ArrayList of FlagQuality objects to return
        // Create an array called flags
        ArrayList<FLAG> flags = new ArrayList<FLAG>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // put in a timeout incase the db is not running
            statement.setQueryTimeout(30);

            // The SQL Query to be executed
            String query = "SELECT * FROM FlagQuality";

            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String flagtype = results.getString("flag");
                String description = results.getString("description");

                // Create an FLAG Object
                FLAG flagsObj = new FLAG(flagtype, description);

                // Add the FLAG object to the flags array
                flags.add(flagsObj);
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

        // Finally we return all of the countries
        return flags;
    }

    // Add your required methods here
    public ArrayList<Persona> getPersonas() {
        ArrayList<Persona> personas = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE);
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);

            String query = "SELECT name, description FROM Personas";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");

                Persona personaObj = new Persona(name, description);
                personas.add(personaObj);
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return personas;
    }

    public ArrayList<TeamMember> getTeamMembers() {
        ArrayList<TeamMember> teamMembers = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE);
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);

            // FIXED TABLE NAME TO MATCH YOUR SCHEMA
            String query = "SELECT name, student_id, subtask, role FROM TeamMembers";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("name");
                String studentNum = rs.getString("student_id");
                String subtask = rs.getString("subtask");
                String role = rs.getString("role");

                TeamMember teamMemberObj = new TeamMember(name, studentNum, subtask, role);
                teamMembers.add(teamMemberObj);
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println("TeamMembers Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return teamMembers;
    }

    public ArrayList<METADATA> getMetadata() {
        // Create the ArrayList of FlagQuality objects to return
        // Create an array called flags
        ArrayList<METADATA> metadata = new ArrayList<METADATA>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // put in a timeout incase the db is not running
            statement.setQueryTimeout(30);

            // The SQL Query to be executed
            String query = "SELECT * FROM Metadata";

            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String field = results.getString("field");
                String description = results.getString("description");

                // Create an FLAG Object
                METADATA metadataObj = new METADATA(field, description);

                // Add the FLAG object to the flags array
                metadata.add(metadataObj);
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

        // Finally we return all of the countries
        return metadata;
    }

    public String getColdestStationName() {
        String result = "No data available";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DATABASE);
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);

            String query = """
                        SELECT l.site, l.name AS Weather_Station, t.minTemp AS Minimum_temperature
                        FROM temperature AS t
                        JOIN location AS l ON t.location = l.site
                        WHERE t.minTemp IS NOT NULL AND TRIM(t.minTemp) <> ''
                        AND t.mintempqual == 'Y'
                        ORDER BY t.minTemp ASC
                        LIMIT 1;
                    """;

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String name = rs.getString("Weather_Station");
                String site = rs.getString("site");
                int temp = rs.getInt("Minimum_temperature");

                result = name + " | " + site + " |  " + temp + "<sup>o</sup>C";
            }

            stmt.close();
        } catch (SQLException e) {
            System.err.println("getFormattedColdestStationName Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return result;
    }

    public String getMostRainfallStationName() {
        String result = "No data available";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DATABASE);
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);

            String query = """
                        Select l.site, l.name as Weather_Station, p.precipitation as precipitation, precipqual as flag
                        from precipitation as p
                        join location as l on p.location = l.site
                        where p.precipitation is not null AND TRIM(p.precipitation) <> ''
                        AND flag == 'Y'
                        order by precipitation desc
                        limit 1;
                    """;

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String name = rs.getString("Weather_Station");
                String site = rs.getString("site");
                double rainfall = rs.getDouble("precipitation");

                result = name + " | Site.No " + site + " | " + rainfall + " mm";
            }

            stmt.close();
        } catch (SQLException e) {
            System.err.println("getMostRainfallStationName Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return result;
    }

    public ArrayList<MetricData> getMetricData(String metric, String startStation, String endStation,
            String startDate, String endDate, String order, String time) {
        ArrayList<MetricData> results = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DATABASE);
            String tableName = metric.toLowerCase();
            String actualColumn = metric;

            if ("Humidity".equals(metric)) {
                tableName = "humidity";
                actualColumn = "humid" + time;
            } else if ("Cloud".equals(metric)) {
                tableName = "cloud";
                actualColumn = "okta" + time;
            } else if (metric.equals("AvgTemp") || metric.equals("MinTemp") || metric.equals("MaxTemp")) {
                tableName = "temperature";
            }

            String query = "SELECT Location, YMD, " + actualColumn + " AS metric_value " +
                    "FROM " + tableName + " " +
                    "WHERE Location BETWEEN '" + startStation + "' AND '" + endStation + "' " +
                    "AND YMD BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                    "ORDER BY " + actualColumn + " " + (order.equals("Asc") ? "ASC" : "DESC") + " LIMIT 50";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            stmt.setQueryTimeout(30);

            while (rs.next()) {
                results.add(new MetricData(
                        rs.getString("metric_value"),
                        rs.getString("YMD"),
                        rs.getString("Location")));
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println("MetricData Error: " + e);
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return results;
    }

    public ArrayList<StateSummary> getStateSummary(String metric, String startStation, String endStation,
            String startDate, String endDate, String order, String time) {
        ArrayList<StateSummary> sum = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DATABASE);
            String tableName = metric.toLowerCase();
            String actualColumn = metric; // Default to metric name

            // Handle special cases
            if ("Humidity".equals(metric)) {
                tableName = "humidity";
                actualColumn = "humid" + time;
            } else if ("Cloud".equals(metric)) {
                tableName = "cloud";
                actualColumn = "okta" + time;
            } else if (metric.equals("AvgTemp") || metric.equals("MinTemp") || metric.equals("MaxTemp")) {
                tableName = "temperature";
            }

            // Build query with correct column reference
            String query = "SELECT L.state, COALESCE(SUM(x." + actualColumn + "), 0) AS total_value " +
                    "FROM Location L " +
                    "LEFT JOIN " + tableName + " x ON L.site = x.Location " +
                    "AND x.YMD BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                    "WHERE L.site BETWEEN '" + startStation + "' AND '" + endStation + "' " +
                    "GROUP BY L.state " +
                    "ORDER BY total_value " + (order.equals("Asc") ? "ASC" : "DESC") + " LIMIT 50";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            stmt.setQueryTimeout(30);

            while (rs.next()) {
                sum.add(new StateSummary(
                        rs.getString("state"),
                        rs.getDouble("total_value")));
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println("StateSummary Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return sum;
    }

    public ArrayList<STATE> getStates() {
        ArrayList<STATE> states = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE);
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);

            String query = """
                    Select Distinct state, stateName
                    from location
                    order by state;
                            """;

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String state = rs.getString("state");
                String stateName = rs.getString("stateName");

                STATE stateInfo = new STATE(state, stateName);

                states.add(stateInfo);
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println("GetStates Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return states;
    }

    public ArrayList<Station> getfilteredStations(String State, double startLat, double endLat, String metric,
            String sortBy, String time) {
        ArrayList<Station> station = new ArrayList<>();

        Connection conn = null;

        String metricTable = "";
        String valueColumn = "";

        if (metric.equalsIgnoreCase("maxTemp")) {
            metricTable = "Temperature";
            valueColumn = "maxTemp";
        }

        else if (metric.equalsIgnoreCase("minTemp")) {
            metricTable = "Temperature";
            valueColumn = "minTemp";
        }

        else if (metric.equalsIgnoreCase("evaporation")) {
            metricTable = "Evaporation";
            valueColumn = "evaporation";
        }

        else if (metric.equalsIgnoreCase("precipitation")) {
            metricTable = "Precipitation";
            valueColumn = "precipitation";
        }

        else if (metric.equalsIgnoreCase("sunshine")) {
            metricTable = "Sunshine";
            valueColumn = "sunshine";
        }

        else if (metric.equalsIgnoreCase("okta")) {
            metricTable = "Cloud";
            valueColumn = metric + time;
        }

        else if (metric.equalsIgnoreCase("humid")) {
            metricTable = "Humidity";
            valueColumn = metric + time;
        }

        else {
            return station;
        }
        try {
            conn = DriverManager.getConnection(DATABASE);

            String query = "SELECT l.site, l.name, l.state, l.lat, l.longt, v.[" + valueColumn + "]" +
                    " FROM location as l JOIN " + metricTable + " as v ON l.site = v.location " +
                    " WHERE l.state = ? AND l.lat BETWEEN ? AND ? " +
                    " GROUP BY l.site " +
                    " ORDER BY " + (sortBy.equals("latitude") ? "l.lat" : "l." + sortBy);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setQueryTimeout(30);

            stmt.setString(1, State);
            stmt.setDouble(2, startLat);
            stmt.setDouble(3, endLat);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String site = rs.getString("site");
                String name = rs.getString("name");
                String state = rs.getString("state");
                double latitude = rs.getDouble("lat");
                double longitude = rs.getDouble("longt");
                String value = rs.getString(valueColumn);

                station.add(new Station(site, name, state, latitude, longitude, value));
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println("GetData Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return station;
    }

    public ArrayList<RegionSummary> getRegionalSummary(String state, double startLat, double endLat, String metric,
            String time) {
        ArrayList<RegionSummary> summaries = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE);

            String metricTable = "";
            String valueColumn = "";

            if (metric.equalsIgnoreCase("maxTemp")) {
                metricTable = "Temperature";
                valueColumn = "maxTemp";
            }

            else if (metric.equalsIgnoreCase("minTemp")) {
                metricTable = "Temperature";
                valueColumn = "minTemp";
            }

            else if (metric.equalsIgnoreCase("evaporation")) {
                metricTable = "Evaporation";
                valueColumn = "evaporation";
            }

            else if (metric.equalsIgnoreCase("precipitation")) {
                metricTable = "Precipitation";
                valueColumn = "precipitation";
            }

            else if (metric.equalsIgnoreCase("sunshine")) {
                metricTable = "Sunshine";
                valueColumn = "sunshine";
            }

            else if (metric.equalsIgnoreCase("okta")) {
                metricTable = "Cloud";
                valueColumn = metric + time;
            }

            else if (metric.equalsIgnoreCase("humid")) {
                metricTable = "Humidity";
                valueColumn = metric + time;
            }

            else {
                return summaries;
            }

            String query = "SELECT l.region, COUNT(l.site) AS station_count, AVG(v." + valueColumn + ") AS avg_metric "
                    +
                    "FROM location l " +
                    "JOIN " + metricTable + " v ON l.site = v.location " +
                    "WHERE l.state = ? AND l.lat BETWEEN ? AND ? " +
                    "GROUP BY l.region " +
                    "ORDER BY l.region;";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setQueryTimeout(30);

            stmt.setString(1, state);
            stmt.setDouble(2, startLat);
            stmt.setDouble(3, endLat);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                summaries.add(new RegionSummary(
                        rs.getString("region"),
                        rs.getInt("station_count"),
                        rs.getDouble("avg_metric")));
            }

            stmt.close();
        } catch (SQLException e) {
            System.err.println("getRegionalSummary Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return summaries;
    }

    public ArrayList<SimilarStation> getSimilarStations(String refStation, String metric,
            int start1, int end1, int start2, int end2, int count, String time) {

        ArrayList<SimilarStation> similarStations = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DATABASE);
            stmt = conn.createStatement();

            String metricTable;
            String valueColumn;

            if (metric.equalsIgnoreCase("maxTemp")) {
                metricTable = "Temperature";
                valueColumn = "maxTemp";
            } else if (metric.equalsIgnoreCase("minTemp")) {
                metricTable = "Temperature";
                valueColumn = "minTemp";
            } else if (metric.equalsIgnoreCase("avgTemp")) {
                metricTable = "Temperature";
                valueColumn = "(maxTemp + minTemp) / 2.0";
            } else if (metric.equalsIgnoreCase("evaporation")) {
                metricTable = "Evaporation";
                valueColumn = "evaporation";
            } else if (metric.equalsIgnoreCase("precipitation")) {
                metricTable = "Precipitation";
                valueColumn = "precipitation";
            } else if (metric.equalsIgnoreCase("sunshine")) {
                metricTable = "Sunshine";
                valueColumn = "sunshine";
            } else if (metric.equalsIgnoreCase("okta")) {
                metricTable = "Cloud";
                valueColumn = "okta" + time;
            } else if (metric.equalsIgnoreCase("humid")) {
                metricTable = "Humidity";
                valueColumn = "humid" + time;
            } else {
                throw new IllegalArgumentException("Invalid metric: " + metric);
            }

            String refQuery = "WITH Period1 AS (" +
                    "    SELECT AVG(" + valueColumn + ") AS avg1 FROM " + metricTable +
                    "    WHERE location = '" + refStation + "' " +
                    "    AND CAST(SUBSTR(YMD, 1, 4) AS INT) BETWEEN " + start1 + " AND " + end1 +
                    "), Period2 AS (" +
                    "    SELECT AVG(" + valueColumn + ") AS avg2 FROM " + metricTable +
                    "    WHERE location = '" + refStation + "' " +
                    "    AND CAST(SUBSTR(YMD, 1, 4) AS INT) BETWEEN " + start2 + " AND " + end2 +
                    ") SELECT avg1, avg2 FROM Period1, Period2;";

            ResultSet refRs = stmt.executeQuery(refQuery);
            if (!refRs.next())
                return similarStations;

            double refAvg1 = refRs.getDouble("avg1");
            double refAvg2 = refRs.getDouble("avg2");
            if (refAvg1 == 0 || Double.isNaN(refAvg1)) {
                System.err
                        .println("Reference station has no valid data for Period 1 — aborting similarity calculation.");
                return similarStations;
            }

            double refChange = (refAvg2 - refAvg1) / refAvg1 * 100.0;
            refRs.close();

            String mainQuery = "WITH Periods AS (" +
                    "    SELECT l.site, l.name, " +
                    "           AVG(CASE WHEN CAST(SUBSTR(t.YMD, 1, 4) AS INT) BETWEEN " + start1 + " AND " + end1 +
                    " THEN " + valueColumn + " END) AS avg1, " +
                    "           AVG(CASE WHEN CAST(SUBSTR(t.YMD, 1, 4) AS INT) BETWEEN " + start2 + " AND " + end2 +
                    " THEN " + valueColumn + " END) AS avg2 " +
                    "    FROM " + metricTable + " t " +
                    "    JOIN location l ON t.location = l.site " +
                    "    GROUP BY l.site" +
                    ") " +
                    "SELECT site, name, avg1, avg2, " +
                    "       ((avg2 - avg1) / avg1 * 100.0) AS percentChange, " +
                    "       ABS(((avg2 - avg1) / avg1 * 100.0) - " + refChange + ") AS similarityScore " +
                    "FROM Periods " +
                    "WHERE avg1 IS NOT NULL AND avg2 IS NOT NULL AND site != '" + refStation + "' " +
                    "ORDER BY similarityScore " +
                    "LIMIT " + count + ";";

            ResultSet rs = stmt.executeQuery(mainQuery);

            while (rs.next()) {
                similarStations.add(new SimilarStation(
                        rs.getString("site"),
                        rs.getString("name"),
                        rs.getDouble("avg1"),
                        rs.getDouble("avg2"),
                        rs.getDouble("percentChange"),
                        rs.getDouble("similarityScore")));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("getSimilarStation Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return similarStations;
    }

    public String stationDrop() {
        ArrayList<STATE> stateList = getStates();
        ArrayList<SITE> siteList = PageCorrelation.getSite();

        String html =   "<label for='sites'><b>Select Weather Station: </b></label>" +
                        "<select name='sites' id='sites' required>" +
                        "<option value='' disabled selected>Weather Station Name</option>";

        int siteNum = 0;

        for (int i = 0; i < stateList.size(); ++i) {
            String stateAbbv = stateList.get(i).getState();
            html += "<optgroup label='" + stateList.get(i).getStateName() + "'>";
            for (int j = siteNum; (j < siteList.size()) && stateAbbv.equals(siteList.get(j).getState()) ; ++j) {
                html += "<option value='" + siteList.get(j).getSite() + "'>" + siteList.get(j).getSiteName() + "</option>";
                siteNum = j+1;
            }
            html += "</optgroup>";
        }

        html += "</select>";

        return html;
    }
}