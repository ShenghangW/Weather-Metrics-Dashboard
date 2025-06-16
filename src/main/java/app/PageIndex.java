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
        <div class="topnav-left">
        <a href="#">
        <div class = "menu">
        <div class = "menu-bar"></div>
        <div class = "menu-bar"></div>
        <div class = "menu-bar"></div>
        </div>
        </a>
        <a href='/'><img class="logo-main" src="logo.png" /></a>
            <div class='dropdown'>
            <span><a href='mission.html'>Our Mission</a></span>
            <div class='dropdown-content'>
            <a href='searchMetric.html'>Metric Explorer</a>
            </div></div>
            <div class='dropdown'>
            <span><a href='data.html'>Our Data</a></span>
            <div class='dropdown-content'>
            <a href='dataquality.html'>Data Quality</a>
            </div></div>
            </div>
                <form class="search" action="/search" method="get">
                    <input class="search-bar" type="text" name="query" placeholder="Search for Data..." />
                        <button class="submit-button" type="submit">
                            <img src="search.png" alt="Search" />
                        </button>
                        <button class="filter-button" onclick="openFilter()">Filter</button>
                </form>
                    <div class="topnav-right">
                        <button class="account-button" onclick="openLogin()">Login</button>
                        <button class="account-button" onclick="openSignup()">Signup</button>
                    </div>
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

        String yearRange = jdbc.getYearRange();
        String coldest = jdbc.getColdestStationName();
        String rainfall = jdbc.getMostRainfallStationName();
        
        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + navbar;

        // Add header content block
        html = html +  """
            <div class='header'>
            <img src='earth-image.jpeg' alt='Weather Background' class='header-bg-image'  draggable =false/>
            <div class='header-overlay'>
            Welcome to AusClimateView
            </div>
            </div>
            """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html += "<div class='slide-show'>" +
        "<div class='slides'>" +

        "<div class='slide'><div class='slide-content'>" +
        "<h2>Year Range</h2><p>" + yearRange + "</p>" +
        "</div></div>" +

        "<div class='slide coldest-bg'><div class='slide-content'>" +
        "<h2>Lowest Recorded Temperature Station</h2><p>" + coldest + "</p>" +
        "</div></div>" +

        "<div class='slide rainfall-bg'><div class='slide-content'>" +
        "<h2>Highest Recorded Rainfall Station</h2><p>" + rainfall + "</p>" +
        "</div></div>" +

        "</div></div>";
        // Close Content div
        html = html + "</div>";

        // Footer
        html = html + """
            <div class='footer'>
                <p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>
            </div>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
