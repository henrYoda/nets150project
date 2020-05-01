import java.util.HashMap;
import java.util.Set;

public class VectorsForSimilarity {

    private Corpus corpus;

    /**
     * The tf-idf weight vectors.
     * The hashmap maps a course to another hashmap.
     * The second hashmap maps a term to its tf-idf weight for this document.
     */
    private HashMap<String, HashMap<String, Double>> tfIdfWeights;


    public VectorsForSimilarity(Corpus corpus) {
        this.corpus = corpus;
        tfIdfWeights = new HashMap<>();

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
            HashMap<String, Double> weights = new HashMap<String, Double>();

            for (String term : terms) {
                double tf = currCourse.getWordFreq(term);
                double idf = corpus.getInverseDocumentFrequency(term);

                double weight = tf * idf;

                weights.put(term, weight);
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

        for (String term : weights1.keySet()) {
            product += weights1.get(term) * weights2.get(term);
        }

        return product;
    }

    /**
     * This will return the cosine similarity of two documents.
     * This will range from 0 (not similar) to 1 (very similar).
     * @param c1 Document 1
     * @param c2 Document 2
     * @return the cosine similarity
     */
    public double cosineSimilarity(Course c1, Course c2) {
        return getDotProduct(c1, c2) / (getMagnitude(c1) * getMagnitude(c2));
    }
}
