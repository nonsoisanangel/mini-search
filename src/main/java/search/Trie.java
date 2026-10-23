package search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * STAGE 4. Prefix tree for autocomplete.
 *
 * insert("search") twice and insert("seam") once, then suggest("sea", 5)
 * should return ["search", "seam"] (most frequent first).
 */
public class Trie {

    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        int count;          // how many times a word ending here was inserted
        boolean isWord;
    }

    private final Node root = new Node();

    /** Insert a word, or bump its count if it is already there. */
    public void insert(String word) {
        // TODO Stage 4
        throw new UnsupportedOperationException("TODO: implement insert");
    }

    /** Up to k completions of the prefix, highest count first. Empty list if none. */
    public List<String> suggest(String prefix, int k) {
        // TODO Stage 4
        throw new UnsupportedOperationException("TODO: implement suggest");
    }
}
