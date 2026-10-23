package search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The deliberately slow baseline. For every query it re-tokenizes and scans
 * EVERY document, keeping the ones that contain ALL query terms (AND search).
 *
 * Cost: O(N * L) per query, where N = number of documents, L = average length.
 * Your inverted index exists to beat this. Keep this class around for benchmarks.
 */
public class NaiveSearcher {

    private final DocumentStore store;
    private final Tokenizer tokenizer;

    public NaiveSearcher(DocumentStore store, Tokenizer tokenizer) {
        this.store = store;
        this.tokenizer = tokenizer;
    }

    /** Returns the ids of documents containing every query term, in ascending id order. */
    public List<Integer> search(String query) {
        List<String> terms = tokenizer.tokenize(query);
        List<Integer> results = new ArrayList<>();
        if (terms.isEmpty()) {
            return results;
        }
        for (int docId = 0; docId < store.size(); docId++) {
            Set<String> words = new HashSet<>(tokenizer.tokenize(store.getText(docId)));
            if (words.containsAll(terms)) {
                results.add(docId);
            }
        }
        return results;
    }
}
