import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VectorsForSimilarity {

    private Corpus corpus;

    /**
     * The tf-idf weight vectors.
     * The hashmap maps a course to another hashmap.
     * The second hashmap maps a term to its tf-idf weight for this document.
     */
    private HashMap<String, HashMap<String, Double>> tfIdfWeights;

    private Map<String, Course> courseMap;

    public VectorsForSimilarity(Corpus corpus, Map<String, Course> courseMap) {
        this.corpus = corpus;
        this.courseMap = courseMap;
        tfIdfWeights = new HashMap<>(courseMap.size());

        createTfIdfWeights();
    }

    /**
     * Creates the hashmap of CourseID strings to hashmaps. The internal hashmaps map
     * every term with its tfidf weight. Based on HW4
     */
    private void createTfIdfWeights() {
        Set<String> terms = corpus.getInvertedIndex().keySet();

        for (String courseString : corpus.getCourses().keySet()) {
            Course currCourse = corpus.getCourses().get(courseString);
            HashMap<String, Double> weights = new HashMap<String, Double>(terms.size());
            for (String term : terms) {
                double tf = currCourse.getWordFreq(term);
                double idf = corpus.getInverseDocumentFrequency(term);
                double weight = tf * idf;
                
                //No need to store the weight and term if the weight is 0 - in fact, if we do,
                //way, way too much memory is used and the program throws a java memory error
                //- Henrique
                if (weight != 0) {
                    weights.put(term, weight);
                }
            }
            tfIdfWeights.put(courseString, weights);
        }
    }

    /**
     * This method will return the magnitude of a vector. Mostly taken from HW4
     * @param course the course whose magnitude is calculated.
     * @return the magnitude
     */
    private double getMagnitude(Course course) {
        double magnitude = 0;
        HashMap<String, Double> weights = tfIdfWeights.get(course.getCourseID());

        for (double weight : weights.values()) {
            magnitude += weight * weight;
        }

        return Math.sqrt(magnitude);
    }

    /**
     * This will take two documents and return the dot product. Mostly taken from HW4
     * @param c1 Course 1
     * @param c2 Course 2
     * @return the dot product of the documents
     */
    private double getDotProduct(Course c1, Course c2) {
        double product = 0;
        HashMap<String, Double> weights1 = tfIdfWeights.get(c1.getCourseID());
        HashMap<String, Double> weights2 = tfIdfWeights.get(c2.getCourseID());

        //Modification needed since we do not save terms with a weight of 0 for space reasons - Henrique
        for (String term : weights1.keySet()) {
            product += weights1.get(term) * (weights2.containsKey(term) ? weights2.get(term) : 0);
        }

        return product;
    }

    /**
     * This will return the cosine similarity of two documents.
     * This will range from 0 (not similar) to 1 (very similar).
     * @param c1String CourseID of the course being compared to search term
     * @param search The course object of the search input
     * @return the cosine similarity
     */
    public double cosineSimilarity(String c1String, Course search) {
        Course c1 = courseMap.get(c1String);
        return getDotProduct(c1, search) / (getMagnitude(c1) * getMagnitude(search));
    }

    /**
     * Helper function for getTenMostSimilar. Finds the smallest value in a hashmap of
     * courseID strings mapped to their similarity with the search course
     * @param topTen The current topTen most similar courses
     * @return The least similar class in this top ten.
     */
    String minSimilarity(HashMap<String, Double> topTen) {
        Set<String> courseSet = topTen.keySet();
        double minSimilarity = Double.MAX_VALUE;
        String minCourse = "";
        for (String courseString : courseSet) {
            if (topTen.get(courseString) < minSimilarity) {
                minSimilarity = topTen.get(courseString);
                minCourse = courseString;
            }
        }
        return minCourse;
    }


    /**
     * Method that returns the top ten most similar courses to the input search
     * @param search The input search as a course object
     * @return An ordered arrayList with the most similar course first and least similar
     * tenth.
     */
    ArrayList<String> getTenMostSimilar(Course search) {
        HashMap<String, Double> topTen = new HashMap<>();
        String minRelevantCourse = "";
        for (String courseString : courseMap.keySet()) {
            double currSimilarity = cosineSimilarity(courseString, search);
            if (topTen.size() < 10) {
                topTen.put(courseString, currSimilarity);
                minRelevantCourse = minSimilarity(topTen);
            } else {
                if (currSimilarity > topTen.get(minRelevantCourse)) {
                    topTen.remove(minRelevantCourse);
                    topTen.put(courseString, currSimilarity);
                    minRelevantCourse = minSimilarity(topTen);
                }
            }
        }

        ArrayList<String> topTenFinal = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            double currMin = Double.MAX_VALUE;
            String currMinString = "";
            for (String courseString : topTen.keySet()) {
                double curr = topTen.get(courseString);
                if (curr < currMin) {
                    currMin = curr;
                    currMinString = courseString;
                }
            }
            topTen.remove(currMinString);
            topTenFinal.add(0, currMinString);
        }

        return topTenFinal;
    }
}
