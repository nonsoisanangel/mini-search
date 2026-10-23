package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NaiveSearcherTest {

    private NaiveSearcher searcher;

    @BeforeEach
    void setUp() {
        DocumentStore store = new DocumentStore();
        store.add("d0", "Java is a programming language");
        store.add("d1", "Python is also a programming language");
        store.add("d2", "Coffee is not a programming language but java is coffee");
        searcher = new NaiveSearcher(store, new Tokenizer());
    }

    @Test
    void singleTermFindsAllMatchingDocs() {
        assertEquals(List.of(0, 2), searcher.search("java"));
    }

    @Test
    void multiTermIsAndSearch() {
        assertEquals(List.of(2), searcher.search("java coffee"));
    }

    @Test
    void queryIsCaseInsensitive() {
        assertEquals(List.of(0, 2), searcher.search("JAVA"));
    }

    @Test
    void unknownTermGivesNoResults() {
        assertTrue(searcher.search("rust").isEmpty());
    }

    @Test
    void emptyQueryGivesNoResults() {
        assertTrue(searcher.search("").isEmpty());
        assertTrue(searcher.search("the").isEmpty()); // only a stopword
    }
}
