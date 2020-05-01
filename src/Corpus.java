import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Corpus {

    private HashMap<String, Course> courseMap;

    // Instead of using the course objects we will keep track of them using their string identifiers
    HashMap<String, Set<String>> invertedIndex;

    public Corpus(HashMap<String, Course> courseMap) {
        this.courseMap = courseMap;
        invertedIndex = new HashMap<>();

        populateInvertedFrequencies();
    }

    /**
     * Goes through every course and makes a hashmap that maps every possible term to a set of Strings
     * which identify the courses that contain that term. Mostly taken from HW4
     */
    private void populateInvertedFrequencies() {
        Set<String> courseSet = courseMap.keySet();

        for (String courseString : courseSet) {

            Course course = courseMap.get(courseString);
            Set<String> terms = course.getTermList();

            for (String term : terms) {
                if (invertedIndex.containsKey(term)) {
                    Set<String> list = invertedIndex.get(term);
                    list.add(courseString);
                } else {
                    Set<String> list = new TreeSet<String>();
                    list.add(courseString);
                    invertedIndex.put(term, list);
                }
            }
        }
    }

    /**
     * This method returns the idf for a given term. Taken from HW4
     * @param term a term in a document
     * @return the idf for the term
     */
    public double getInverseDocumentFrequency(String term) {
        if (invertedIndex.containsKey(term)) {
            double size = courseMap.size();
            Set<String> list = invertedIndex.get(term);
            double documentFrequency = list.size();

            return Math.log10(size / documentFrequency);
        } else {
            return 0;
        }
    }

    /**
     * This is a method for adding our input search string into the corpus.
     * @param search The string that was input by the user. We eventually want to find the Courses
     *               that are most similar to this string. This string's terms need to be included
     *               in the corpus, however, for an accurate comparison.
     */
    public void addSearchToCorpus(Course search) {
        Set<String> terms = search.getTermList();

        for (String term : terms) {
            if (invertedIndex.containsKey(term)) {
                Set<String> list = invertedIndex.get(term);
                list.add("Search");
            } else {
                Set<String> list = new TreeSet<String>();
                list.add("Search");
                invertedIndex.put(term, list);
            }
        }
    }

    /**
     * @return the invertedIndex
     */
    public HashMap<String, Set<String>> getInvertedIndex() {
        return invertedIndex;
    }

    /**
     * @return the courseID strings
     */
    public HashMap<String, Course> getCourses() {
        return courseMap;
    }

}
