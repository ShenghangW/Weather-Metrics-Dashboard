package app;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 * @editor David Eccles, 2025. email: david.eccles@rmit.edu.au
 */
public class PageIndex implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/";

   public static String navbar = """
        <div class="topnav">
            <a href='/'><img class="logo-main" src="logo.png" alt="Home" /></a>
            <a class="nav-button" href="mission.html">Our Mission</a>
            <a class="nav-button" href="data.html">Data</a>
            <a class="nav-button" href="dataquality.html">Data Quality</a>
            <a class="nav-button" href="searchResults.html">Station Data</a>
            <a class="nav-button" href="metricExplorer.html">Metric Explorer</a>
            <a class="nav-button" href="StationSimilarity.html">Station Similarity</a>
            <a class="nav-button" href="metricSimilarity.html">Metric Similarity</a>
            <a class="nav-button" href="correlation.html">Data Correlation</a>
        </div>
        """;
    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Header information
        html = html + "<head>" + 
            "<title>Homepage</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        JDBCConnection jdbc = new JDBCConnection();
        String coldest = jdbc.getColdestStationName();
        String rainfall = jdbc.getMostRainfallStationName();
        
        // Add the body
        html = html + "<body>";

        // Add the topnav
        html = html + navbar;

        // Add header content block
        html = html +  """
            <div class='header'>
            <img src='earth-image.jpeg' alt='Weather Background' class='header-bg-image'  draggable =false/>
            <div class='header-overlay'>
            Welcome to Weather Report
            </div>
            </div>
            """;

        // Add Div for page Content
        html = html + "<div class='content'>";
        
        // Add a new section for featured pages
        html += """
            <div class='featured-section'>
                <div class='featured-item'>
                    <img src='waveMout.png' alt='Mission' class='featured-image' draggable=false />
                    <div class='featured-text'>
                        <h2>Our Mission</h2>
                        <p>Learn more about our mission and goals.</p>
                        <a href='mission.html' class='featured-link'>Explore Mission</a>
                    </div>
                </div>

                <div class='featured-item'>
                    <img src='datapageheaderbg.jpg' alt='Data' class='featured-image' draggable=false />
                    <div class='featured-text'>
                        <h2>Data Insights</h2>
                        <p>Explore the latest weather data and trends.</p>
                        <a href='data.html' class='featured-link'>View Data</a>
                    </div>
                </div>
            </div>
        """;

        // Add HTML for the page content
        html += "<div class='slide-show'>" +
        "<div class='slides'>" +
        "<div class='slide year-image'><div class='slide-content'>" +
        "<h2>Year Range</h2><p>1970-2020</p>" +
        "</div></div>" +

        "<div class ='slide coldest-bg'><div class='slide-content'>" +
        "<h2>Lowest Recorded Temperature Station</h2><p>" + coldest + "</p>" +
        "</div></div>" +

        "<div class ='slide rainfall-bg'><div class='slide-content'>" +
        "<h2>Highest Recorded Rainfall Station</h2><p>" + rainfall + "</p>" +
        "</div></div>" +

        "</div></div>";
        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
            <div class='footer'>
                <p>Weather Report(2025)</p>
            </div>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
