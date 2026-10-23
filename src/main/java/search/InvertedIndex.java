package search;

import java.util.List;

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

    /** Index one document. Call with increasing docIds (0, 1, 2, ...). */
    public void addDocument(int docId, List<String> tokens) {
        // TODO Stage 2
        throw new UnsupportedOperationException("TODO: implement addDocument");
    }

    /** Posting list for a term, or an empty list if the term never appears. */
    public List<Posting> getPostings(String term) {
        // TODO Stage 2
        throw new UnsupportedOperationException("TODO: implement getPostings");
    }

    /** Number of documents that contain the term (df). */
    public int docFreq(String term) {
        // TODO Stage 2
        throw new UnsupportedOperationException("TODO: implement docFreq");
    }

    /** Total number of documents indexed (N). */
    public int documentCount() {
        // TODO Stage 2
        throw new UnsupportedOperationException("TODO: implement documentCount");
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
