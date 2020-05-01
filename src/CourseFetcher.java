import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



public class CourseFetcher {

    private final static CloseableHttpClient httpClient = HttpClients.createDefault();
    
    private static String AUTH_ID = "UPENN_OD_enAW_1004944";
    private static String AUTH_PASSWORD = "5rbpm575cgmmn5btonf49i3p7l";

    public static void main(String[] args) throws IOException {
        
        HttpGet httpget = new HttpGet("https://esb.isc-seo.upenn.edu/8091/open_data/course_info/ACCT/");
        
        httpget.setHeader("Authorization-Bearer", AUTH_ID);
        httpget.setHeader("Authorization-Token", AUTH_PASSWORD);
        
        HttpResponse response = httpClient.execute(httpget);
        
        String rs = EntityUtils.toString(response.getEntity());
        System.out.println(rs);
    }

    
}
