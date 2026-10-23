package search;

import java.util.List;

/**
 * STAGE 3. Scores documents with TF-IDF and returns the top k.
 *
 *   score(doc, query) = sum over query terms t of:  tf(t, doc) * log(N / df(t))
 *
 * Use a min-heap (PriorityQueue) of size k so you never sort all results:
 *   push each scored doc; if heap size > k, pop the smallest.
 *   That is O(n log k) instead of O(n log n).
 */
public class Ranker {

    public record ScoredDoc(int docId, double score) {}

    private final InvertedIndex index;

    public Ranker(InvertedIndex index) {
        this.index = index;
    }

    /** Top k documents for the query terms, best score first. */
    public List<ScoredDoc> topK(List<String> terms, int k) {
        // TODO Stage 3
        throw new UnsupportedOperationException("TODO: implement topK");
    }
}
