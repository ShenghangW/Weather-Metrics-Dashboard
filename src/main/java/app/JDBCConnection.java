    package app;

    import java.util.ArrayList;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;

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
                    String flagtype     = results.getString("flag");
                    String description  = results.getString("description");

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
                if (conn != null) conn.close();
            } catch(SQLException e) {
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
            } catch(SQLException e) {
                System.err.println("TeamMembers Error: " + e.getMessage());
            } finally {
                try {
                    if (conn != null) conn.close();
                } catch(SQLException e) {
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
                    String field     = results.getString("field");
                    String description  = results.getString("description");

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

    public String getYearRange() {
        String yearRange = "Unknown";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DATABASE);
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);

            String query = """
                    select min(STRFTIME('%Y',date)) as minyear, max(STRFTIME('%Y',date)) as maxyear
                    from datetime
                    limit 1;
                    """;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                int minYear = rs.getInt("minYear");
                int maxYear = rs.getInt("maxYear");
                yearRange = minYear + " - " + maxYear;
            }

            stmt.close();
        } catch (SQLException e) {
            System.err.println("getYearRange Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return yearRange;
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
                if (conn != null) conn.close();
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
            System.err.println("getFormattedMostRainfallStationName Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
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
        }
        else if ("Cloud".equals(metric)) {
            tableName = "cloud";
            actualColumn = "cloud_okta" + time;
        }
        else if (metric.equals("AvgTemp") || metric.equals("MinTemp") || metric.equals("MaxTemp")) {
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

        while(rs.next()) {
            results.add(new MetricData(
                rs.getString("metric_value"),  // Use the alias
                rs.getString("YMD"),
                rs.getString("Location")
            ));
        }
        stmt.close();
    } catch(SQLException e) {
        System.err.println("MetricData Error: " + e);
        e.printStackTrace();
    } finally {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch(SQLException e) {
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
        String actualColumn = metric;  // Default to metric name
        
        // Handle special cases
        if ("Humidity".equals(metric)) {
            tableName = "humidity";
            actualColumn = "humid" + time;
        }
        else if ("Cloud".equals(metric)) {
            tableName = "cloud";
            actualColumn = "cloud_okta" + time;
        }
        else if (metric.equals("AvgTemp") || metric.equals("MinTemp") || metric.equals("MaxTemp")) {
            tableName = "temperature";
        }

        // Build query with correct column reference
        String query = "SELECT L.state, COALESCE(SUM(x." + actualColumn + "), 0) AS total_value " +
                        "FROM Location L " +
                        "LEFT JOIN " + tableName + " x ON L.site = x.Location " +
                        "WHERE L.site BETWEEN '" + startStation + "' AND '" + endStation + "' " +
                        "AND x.YMD BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                        "GROUP BY L.state " +
                        "ORDER BY total_value " + (order.equals("Asc") ? "ASC" : "DESC") + " LIMIT 50";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        stmt.setQueryTimeout(30);

        while(rs.next()) {
            sum.add(new StateSummary(
                rs.getString("state"),
                rs.getDouble("total_value")  // Use the alias
            ));
        }
        stmt.close();
    } catch(SQLException e) {
        System.err.println("StateSummary Error: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    return sum;
    }
}