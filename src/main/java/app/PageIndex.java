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

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """    
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
            <a href='mission.html'>Our Mission</a>
            <a href='equipment.html'>Our Data</a>
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
                <div class="header"> 
                <img class="header-bg-image" src="earth-image.jpeg" alt="header photo">
                    <div class="header-overlay">
                        <h1>HomePage</h1>
                    </div>
                </div>
        """;

        // Add header content block
        

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
            <p>Homepage content</p>
            """;

        // Finish the List HTML
        html = html + "</ul>";

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
