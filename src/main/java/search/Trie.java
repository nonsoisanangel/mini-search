package search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

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
        Node current = root;
        for (char c : word.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new Node());
        }
        current.isWord = true;
        current.count++;
    }

    /** Up to k completions of the prefix, highest count first. Empty list if none. */
    public List<String> suggest(String prefix, int k) {
        Node current = root;
        for (char c : prefix.toCharArray()) {
            current = current.children.get(c);
            if (current == null) {
                return new ArrayList<>(); // no word starts with this prefix
            }
        }

        List<Map.Entry<String, Integer>> found = new ArrayList<>();
        collect(current, new StringBuilder(prefix), found);

        // most frequent first; ties broken alphabetically
        found.sort((x, y) -> {
            int byCount = Integer.compare(y.getValue(), x.getValue());
            return byCount != 0 ? byCount : x.getKey().compareTo(y.getKey());
        });

        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, found.size()); i++) {
            result.add(found.get(i).getKey());
        }
        return result;
    }
    private void collect(Node node, StringBuilder path, List<Map.Entry<String, Integer>> out) {
        if (node.isWord) {
            out.add(Map.entry(path.toString(), node.count));
        }
        for (Map.Entry<Character, Node> e : node.children.entrySet()) {
            path.append(e.getKey());
            collect(e.getValue(), path, out);
            path.deleteCharAt(path.length() - 1);
        }
    }
}
