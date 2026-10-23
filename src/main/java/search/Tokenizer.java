package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Turns raw text into a list of normalized search terms.
 * Lowercases, splits on anything that isn't a letter or digit, and drops stopwords.
 */
public class Tokenizer {

    private static final Set<String> STOPWORDS = Set.of(
            "a", "an", "and", "are", "as", "at", "be", "by", "for", "from",
            "has", "he", "in", "is", "it", "its", "of", "on", "that", "the",
            "to", "was", "were", "will", "with");

    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        if (text == null) {
            return tokens;
        }
        for (String raw : text.toLowerCase().split("[^a-z0-9]+")) {
            if (raw.isEmpty() || STOPWORDS.contains(raw)) {
                continue;
            }
            tokens.add(raw);
        }
        return tokens;
    }
}
