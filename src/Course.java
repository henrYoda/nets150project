import java.util.HashMap;
import java.util.Scanner;

/**
 * A class to represent Penn classes
 * Should store Pre-reqs as children, class title, class description
 */
public class Course {

    String title;
    String description;
    Course[] prereqs;

    HashMap<String, Integer> wordFreq;

    public Course(String title, String description, Course[] prereqs) {
        this.title = title;
        this.description = description;
        this.prereqs = prereqs;
    }

    /**
     * Do we need a method similar to 'readFileAndProcess'?
     * The equivalent of just putting a filename into the constructor and then having a method
     * which extracts all that info from the file into this class? Otherwise all the info is
     * going to have to be extracted before the class is made which might be tedious in a main
     * method elsewhere.
     */

    /**
     * I am making this method take in a string as I'm not sure how we will construct this class
     * yet. If we pass in a string as I have done then this obviously won't need a parameter.
     * If we pass it a file or something then this might be useful for actually creating the
     * description string as well as finding the word frequency hashMap.
     * Most of this method is taken from the document class in HW4
     * @param description This is some body of text - maybe a string. We want to find the frequency
     *                    with which each word occurs.
     */
    void getWordFreq(String description) {
        Scanner in = new Scanner(description);
        while (in.hasNext()) {
            String nextWord = in.next();
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
}