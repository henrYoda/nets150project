import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    
    /**
     * This is the main method. Here, we get user input and process it accordingly, returning the
     * data he is requesting. We first ask the user to type in what he is interested in learning at Penn.
     * We then give him courses at Penn that are most relevant to his search. He can then type the name of 
     * a course to learn more about it and see the order in which he needs to take his requirements to take
     * that class.
     * @author Henrique Lorente
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        System.out.println("Welcome to Penn Course Helper! Please wait while we update our database of courses.");
        Map<String, Course> courseMap = CourseFetcher.fetchAndBuildCourseMap();
        
        //Generate corpus of courses
        Corpus corpus = new Corpus(courseMap);
        
        System.out.println();
        
        System.out.println("Please enter what you are interest in learning at Penn (e.g. \"Machine Learning\"): ");

        
        
        String input = "";
        Scanner sc = new Scanner(System.in);
        input = sc.nextLine();
        
        System.out.println("Searching...");

        Course searchQuery = new Course(input);
        
        //Get the most similar courses to his search.
        corpus.addSearchToCorpus(searchQuery, courseMap);
        VectorsForSimilarity v = new VectorsForSimilarity(corpus, courseMap);
        ArrayList<String> relevantCourses = v.getTenMostSimilar(searchQuery);
        
        //Return the most relevant courses
        System.out.println("Here are the most relevant courses to your query: ");
        for (int i = 1; i < 10; i++) {
            String courseID = relevantCourses.get(i);
            String courseTitle = courseMap.get(courseID).getTitle();
            
            System.out.println(i + ") " + courseID + ": " + courseTitle);
            System.out.println();
        }
        
        System.out.println("Now, please type the number of the course you want to learn more about (e.g. '1'): ");
        input = sc.nextLine();

        
        int index = Integer.parseInt(input);
        String courseID = relevantCourses.get(index);
        String courseTitle = courseMap.get(courseID).getTitle();
        String courseDescription = courseMap.get(courseID).getDescription();
        
        
        //Get the prereqs for this course
        PrereqBuilder builder = new PrereqBuilder(courseID, courseMap);
        List<String> courses = builder.getTopologicalSorting();
        
        System.out.println("You have selected " + courseID);
        System.out.println();
        System.out.println(courseID + " - " + courseTitle);
        System.out.println("Description: " + makeReadable(courseDescription));
        System.out.print("Order of pre-requisites to take before taking this course: ");
        for (int i = 0; i < courses.size(); i++) {
            String c = courses.get(i);
            System.out.print(c + (i != courses.size() - 1 ? " -> " : ""));
        }
        System.out.println();
        System.out.println("Thanks for using our program! Made by Henrique, Pranav and Kit.");
        /*
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
        sc.close();*/
    }
    
    /**
     * This is a function that we use to break up descriptions into multiple lines, so that they are
     * more readable when printed to the user.
     * @author Henrique Lorente
     * @param text - Text that we want to break up into multiple lines
     *  */
    static String makeReadable(String text) {
        StringBuilder builder = new StringBuilder(text);
        int counter = 0;
        for (int i = 0; i < text.length(); i++) {
            counter++;
            if (counter > 120) {
                if (builder.charAt(i) == ' ') {
                    builder.setCharAt(i, '\n');
                    counter = 0;
                }
            }
        }
        
        return builder.toString();
    }
}
