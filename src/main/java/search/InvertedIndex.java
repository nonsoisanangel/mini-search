package search;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * STAGE 2. Maps each term to the list of documents that contain it.
 *
 * Suggested internal structure:
 *     Map<String, List<Posting>> index = new HashMap<>();
 *
 * Key facts to use:
 *  - You add documents in increasing docId order, so every posting list is
 *    automatically sorted by docId. Don't re-sort it.
 *  - Count term frequencies per document first (Map<String,Integer>), then add
 *    one Posting per distinct term.
 */
public class InvertedIndex {
    private final Map<String, List<Posting>> index = new HashMap<>();
    private int documentCount = 0;

    /** Index one document. Call with increasing docIds (0, 1, 2, ...). */
    public void addDocument(int docId, List<String> tokens) {
        Map<String, Integer> counts = new HashMap<>();
        for (String t : tokens) {
            counts.merge(t, 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            index.computeIfAbsent(e.getKey(), k -> new ArrayList<>())
                 .add(new Posting(docId, e.getValue()));
        }
        documentCount++;
    }

    /** Posting list for a term, or an empty list if the term never appears. */
    public List<Posting> getPostings(String term) {
        return index.getOrDefault(term, List.of());

    }

    /** Number of documents that contain the term (df). */
    public int docFreq(String term) {
        return getPostings(term).size();
    }

    /** Total number of documents indexed (N). */
    public int documentCount() {
        return documentCount;
    }

    /**
     * Docs containing ALL terms, in ascending docId order.
     * Hint: intersect posting lists with two pointers (O(m + n)).
     * Start with the rarest term (smallest list) to shrink the work.
     */
    public List<Integer> searchAnd(List<String> terms) {
        // TODO Stage 2
        throw new UnsupportedOperationException("TODO: implement searchAnd");
    }
}
