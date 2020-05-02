import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class CourseFetcher {

    private final static CloseableHttpClient httpClient = HttpClients.createDefault();
    
    //Authentication details needed to interface with Penn API
    private static String AUTH_ID = "UPENN_OD_enAW_1004944";
    private static String AUTH_PASSWORD = "5rbpm575cgmmn5btonf49i3p7l";
    
    /**
     * This method will fetch a list of all course departments there are at Penn (like CIS, ACCT, MATH, etc.)
     * It will return this list so that it can be used to fetch all courses at Penn, by department
     * @author Henrique Lorente
     * @return an ArrayList of Strings of all course departments at Penn
     * @throws IOException
     */
    private static ArrayList<String> getCourseDepartments() throws IOException {
        HttpGet httpget = new HttpGet("https://esb.isc-seo.upenn.edu/8091/open_data/course_section_search_parameters/");
        
        httpget.setHeader("Authorization-Bearer", AUTH_ID);
        httpget.setHeader("Authorization-Token", AUTH_PASSWORD);
        
        HttpResponse response = httpClient.execute(httpget);
        
        String searchParamsJSONString = EntityUtils.toString(response.getEntity());
        JSONObject searchParamsObject = new JSONObject(searchParamsJSONString);

        JSONArray resultArray = searchParamsObject.getJSONArray("result_data");
        
        JSONObject departmentMap = resultArray.getJSONObject(0).getJSONObject("departments_map");
        
        ArrayList<String> result = new ArrayList<String>();
        
        for (String k : departmentMap.keySet()) {
            result.add(k);
        }
        
        return result;
    }
    /**
     * This function will fetch all of the courses from each department and extract the relevant data.
     * It will return a map relating course ID's to a corresponding Course object.
     * @author Henrique Lorente
     * @return a map relating course ID's (e.g. "MATH 104") with their respective Course objects
     * @throws IOException
     */
    public static Map<String, Course> fetchAndBuildCourseMap() throws IOException {
        
        System.out.println("Fetching course data...");
        //Fetch list of departments at Penn
        ArrayList<String> departmentList = getCourseDepartments();
        
        Map<String, Course> courseMap = new HashMap<String, Course>();
        
        //Get all the courses in each department
        for (String department : departmentList) {
            HttpGet httpget = new HttpGet("https://esb.isc-seo.upenn.edu/8091/open_data/course_info/" 
            + department + "?&number_of_results_per_page=1000");
            
            httpget.setHeader("Authorization-Bearer", AUTH_ID);
            httpget.setHeader("Authorization-Token", AUTH_PASSWORD);
            
            HttpResponse response = httpClient.execute(httpget);
            
            String coursesJSONString = EntityUtils.toString(response.getEntity());
            JSONObject coursesJSONObject = new JSONObject(coursesJSONString);
            
            JSONArray courseArray = coursesJSONObject.getJSONArray("result_data");
            
            //Loop through each course in the department
            for (int i = 0; i < courseArray.length(); i++) {
                JSONObject courseObject = courseArray.getJSONObject(i);
                String courseNumber = courseObject.getString("course_number");
                String prereqsUnformatted = courseObject.getString("prerequisites");
                
                
                //Get the course id (e.g. MATH 114)
                String courseID = department + " " + courseNumber;
                //Parse the prereqs
                List<String> prereqs = PrereqParser.makeClean(prereqsUnformatted);
                //Course title
                String courseTitle = courseObject.getString("course_title");
                //Course description
                String courseDescription = courseObject.getString("course_description");
                
                //Create the Course object and populate the map
                Course course = new Course(courseID, courseTitle, courseDescription, prereqs);
                courseMap.put(courseID, course);
            }
            
        }
        System.out.println("Done!");
        return courseMap;
    }
}
