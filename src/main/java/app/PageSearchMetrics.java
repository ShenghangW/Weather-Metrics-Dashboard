package app;

import java.util.ArrayList;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageSearchMetrics implements Handler {
    public static final String URL = "/searchMetric.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "<html><head><titleMertic Explorer</title>"
                + "<link rel='stylesheet' type='text/css' href='common.css' />"
                + "</head><body>"

                + PageIndex.navbar

                + "<br>"
                + "<div class='pageRef'>"
                    + "<a href='/'>Home/</a>"
                    + "<a href='mission.html'>Metric Explorer</a>"
                + "</div>"
                    
                + "<div><h1 class='metricExplorerHeader'>Metric Explorer</h1></div>";
        
        html += "<div class='footer'>"
            + "<p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>"
            + "</div></body></html>"; 

        context.html(html);
    }
}
