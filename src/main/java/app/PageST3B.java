package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageST3B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/metricSimilarity.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
                "<title>Subtask B Level 3</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html += PageIndex.navbar;

        // Add header content block
        html = html + """
        <div class='pageRef'><a href='/'>Home/</a>
        <a href='metricSimilarity.html'>page3B</a>
        </div>
        
        <div><h1 class='level3Heading'>Compare Metric Silimilarity</h1></div>

        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
            <h2 class=form-heading2>Explore and Find Metric Similarity across 2 Time periods</h2>

            <form action='/metricSimilarity' method='POST'>
            <div class='form-group'>
            <lable for='metric'>Select a metric:</lable>
            <select name='metric' id='metric' required>
            <option value='Precipitation'>Precipitaion</option>
            <option value='Evaporation'>Evaporation</option>
            <option value='MinTemp'>Temperature (Min)</option>
            <option value-'MaxTemp'>Temperature (Max)</option>
            <option value='Humidity'>Humidity</option>
            <option value='Sunshine'>Sunshine</option>
            <option value='Cloud'>Cloud</option>
            </select></div>

            <div class='form-group'>
            <lable for='period1'>Select a range for the First Period</lable>
            <input name='startDate1' type='date' id='period1'> to
            <input name='endDate1' type='date' id='endDate1'>
            </div>

            <div class='form-group'>
            <lable for='period2'>Select a range for the Second Period</lable>
            <input name='startDate2' type='date' id='period2'></input> to
            <input name='endDate2' type='date' id='endDate2'></input>
            </div>

            <div class='form-group'>
            <lable for='order'>Order in<l/lable>
            <select name='order' id='order'>
            <option value='Asc'>Ascending</option>
            <option value='Desc'>Descending</option>
            </select></div>

            <br>
            <button class='form-group' type='submit'>Analyse Metric</button>
            </form>



            """;

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
