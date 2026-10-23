package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Stage 4: remove this line when you start Trie")
class TrieTest {

    private Trie trie;

    @BeforeEach
    void setUp() {
        trie = new Trie();
        trie.insert("search");
        trie.insert("search");
        trie.insert("search");
        trie.insert("seam");
        trie.insert("seam");
        trie.insert("sea");
        trie.insert("tree");
    }

    @Test
    void suggestsMostFrequentFirst() {
        assertEquals(List.of("search", "seam", "sea"), trie.suggest("sea", 5));
    }

    @Test
    void respectsK() {
        assertEquals(List.of("search", "seam"), trie.suggest("sea", 2));
    }

    @Test
    void prefixThatIsAlsoAWordIsIncluded() {
        assertTrue(trie.suggest("sea", 5).contains("sea"));
    }

    @Test
    void unknownPrefixGivesEmptyList() {
        assertTrue(trie.suggest("xyz", 5).isEmpty());
    }

    @Test
    void emptyPrefixSuggestsOverallMostFrequent() {
        assertEquals("search", trie.suggest("", 1).get(0));
    }
}
