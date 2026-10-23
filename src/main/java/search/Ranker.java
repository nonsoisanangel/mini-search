package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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
        Map<Integer, Double> scores = new HashMap<>();
        int N = index.documentCount();

        for (String term : terms) {
            List<Posting> postings = index.getPostings(term);
            int df = postings.size();
            if (df == 0) continue; // skip terms not in the index
            double idf = Math.log((double) N / df);

            for (Posting posting : postings) {
                scores.merge(posting.docId(), posting.tf() * idf, Double::sum);
            }
        }

        PriorityQueue<ScoredDoc> heap = new PriorityQueue<>(k, (a, b) -> Double.compare(a.score(), b.score()));
        for (Map.Entry<Integer, Double> entry : scores.entrySet()) {
            heap.add(new ScoredDoc(entry.getKey(), entry.getValue()));
            if (heap.size() > k) {
                heap.poll();
            }
        }

        List<ScoredDoc> result = new ArrayList<>();
        while (!heap.isEmpty()) {
            result.add(heap.poll());
        }
        Collections.reverse(result); // best score first
        return result;
    }
}
