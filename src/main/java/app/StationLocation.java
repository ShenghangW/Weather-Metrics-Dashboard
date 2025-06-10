package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Stand-alone Java file for processing the database CSV files.
 * <p>
 * You can run this file using the "Run" or "Debug" options
 * from within VSCode. This won't conflict with the web server.
 * <p>
 * This program opens a CSV file from the Closing-the-Gap data set
 * and uses JDBC to load up data into the database.
 * <p>
 * To use this program you will need to change:
 * 1. The input file location
 * 2. The output file location
 * <p>
 * This assumes that the CSV files are the the **database** folder.
 * <p>
 * WARNING: This code may take quite a while to run as there will be a lot
 * of SQL insert statments!
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au

 */
public class StationLocation {

   // MODIFY these to load/store to/from the correct locations

   private static final String DATABASE = "jdbc:sqlite:database/climate.db";
   private static final String CSV_FILE = "database/location.csv";

   public static void main(String[] args) {
      // The following arrays define the order of columns in the INPUT CSV.
      // The order of each array MUST match the order of the CSV.
      // These are specific to the given file and should be changed for each file.
      // This is a *simple* way to help you get up and running quickly wihout being confusing

      // JDBC Database Object
      Connection connection = null;

      // Like JDBCConnection, we need some error handling.
      try {
         // Open A CSV File to process, one line at a time
         // CHANGE THIS to process a different file
         Scanner lineScanner = new Scanner(new File(CSV_FILE));

         // Read the first line of "headings"
         String header = lineScanner.nextLine();
         System.out.println("Heading row" + header + "\n");

         // Setup JDBC
         // Connect to JDBC data base
         connection = DriverManager.getConnection(DATABASE);

         // Read each line of the CSV
         int row = 1;
         while (lineScanner.hasNext()) {
            // Always get scan by line
            String line = lineScanner.nextLine();

            // Create a new scanner for this line to delimit by commas (,)
            Scanner rowScanner = new Scanner(line);
            rowScanner.useDelimiter(",");

            // Save the Weather Station Site ID
            String site = rowScanner.next();

            // Save the Weather Station Name
            String name = rowScanner.next();

            // Save the Weather Station Lattitude
            String lat = rowScanner.next();

            // Save the Weather Station Longitude
            String longt = rowScanner.next();

            // Save the Weather Station State or Territory
            String state = rowScanner.next();

            // Save the Weather Station Region
            String region = rowScanner.next();

            // Go through the data for the row
            // If we run out of categories, stop for safety (so the code doesn't crash)
            while (rowScanner.hasNext()) {
               String count = rowScanner.next();
            }
            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // statement.setQueryTimeout(30);

            // Create Insert Statement
            String query = "INSERT into Location VALUES ("
                  + site + ","
                  + "'" + name + "',"
                  + "'" + lat + "',"
                  + "'" + longt + "',"
                  + "'" + state + "',"
                  + "'" + region + "')";
            System.out.println(query);
            // Execute the INSERT
            System.out.println("Executing: " + query);
            statement.execute(query);

            row++;
         }

      } catch (

      Exception e) {
         e.printStackTrace();
      }
   }
}
