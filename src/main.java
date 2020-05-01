

public class main {

    public static void main(String[] args) {

    }


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
}
