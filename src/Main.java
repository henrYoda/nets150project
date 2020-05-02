import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    
    /**
     * This is the main method. Here, we get user input and process it accordingly, returning the
     * data he is requesting.
     * @author Henrique Lorente
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        System.out.println("Welcome! Please wait while we update our database of courses.");
        Map<String, Course> courseMap = CourseFetcher.fetchAndBuildCourseMap();
        
        System.out.println();
        
        
        String input = "";
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a course title (e.g. MATH 104) or type \"quit\" to exit");
        input = sc.nextLine();
        
        while (!input.equals("quit")) {
            PrereqBuilder builder = new PrereqBuilder(input, courseMap);
            System.out.println("Here is the order of courses you need to take before you take " + input + ":");
            
            List<String> courses = builder.getTopologicalSorting();
            
            for (int i = 0; i < courses.size(); i++) {
                String c = courses.get(i);
                System.out.print(c + (i != courses.size() - 1 ? " -> " : ""));
            }
            System.out.println();
            System.out.println();
            System.out.println("Please enter a course title (e.g. MATH 104) or type \"quit\" to exit");
            input = sc.nextLine();
        }
        System.out.println("Thank you for using our program.");
        sc.close();
    }
}
