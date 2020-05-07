import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/*
 * Notes by Kit on how similar course finder will work...
 * The course objects are made in courseFetcher (?) These will be put into a hashmap
 * where the key is the string identifier (e.g. ACCT 101).
 * Once we have this hashmap, we will create a corpus using this hashmap which will create
 * another hashmap containing the inverseIndices which can be used to find the IDF for a
 * specific term.
 * Create a 'Course' object using the special constructor for the search term
 * We will then add the search string to the corpus. It will be identified by a 'courseID'
 * that is "Search"
 * At this stage we will have a corpus that contains the inverseIndex for every word in
 * any Course, including the search. We will also have a course object representing the search.
 * We will then have a class dedicated to finding the cosine similarity between two courses.
 * This will involve finding the tfid weights for every term in every course. We will then use
 * this info to find the cosine similarity between the search course and every other course.
 * Finally, we will return a List of the 10 most similar courses.
 */

/**
 * A class to represent Penn classes
 * Should store Pre-reqs as children, class title, class description
 */
public class Course {

    private String courseID;
    private List<String> prereqs;
    private String description;
    private String title;
    Map<String, Integer> wordFreq;

    public Course(String courseID, String title, String description, List<String> prereqs) {
        this.courseID = courseID;
        this.prereqs = prereqs;
        this.title = title;
        this.description = description;
        
        wordFreq = new HashMap<>();
        populateWordFreq(title);
        populateWordFreq(description);
    }

    /**
     * Extra constructor for turning the search query into a 'course' so that it's text
     * can be effectively compared to the text from other courses
     * @param search The search string input by the user which will dictate which courses we
     *               return.
     */
    public Course(String search) {
        this.courseID = "Search";
        wordFreq = new HashMap<>();
        
        populateWordFreq(search);
    }

    /**
     * I am making this method take in a string as I'm not sure how we will construct this class
     * yet. If we pass in a string as I have done then this obviously won't need a parameter.
     * If we pass it a file or something then this might be useful for actually creating the
     * description string as well as finding the word frequency hashMap.
     * Most of this method is taken from the document class in HW4
     * @param text This is some body of text - maybe a string. We want to find the frequency
     *                    with which each word occurs.
     */
    private void populateWordFreq(String text) {
        Scanner inDescription = new Scanner(text);
        while (inDescription.hasNext()) {
            String nextWord = inDescription.next();
            String filteredWord = nextWord.replaceAll("[^A-Za-z0-9]", "").toLowerCase();

            if (!(filteredWord.equalsIgnoreCase(""))) {
                if (wordFreq.containsKey(filteredWord)) {
                    int oldCount = wordFreq.get(filteredWord);
                    wordFreq.put(filteredWord, ++oldCount);
                } else {
                    wordFreq.put(filteredWord, 1);
                }
            }
        }
    }

    /**
     * Finds the frequency with which a specific word has been used between both the title and the description
     * Mostly taken from HW4
     * @param word The word whose frequency we are interested in
     * @return The frequency of that word
     */
    public double getWordFreq(String word) {
        if (wordFreq.containsKey(word)) {
            return wordFreq.get(word);
        } else {
            return 0;
        }
    }

    public Set<String> getTermList() {
        return wordFreq.keySet();
    }


    public String getCourseID() {
        return courseID;
    }

    public List<String> getPrereqs() {
        return prereqs;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getTitle() {
        return title;
    }
}