package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Write the implementation until these pass, then delete the @Disabled lines.
 * Add your own edge cases too (empty token list, repeated terms, 3-term queries).
 */
@Disabled("Stage 2: remove this line when you start InvertedIndex")
class InvertedIndexTest {

    private InvertedIndex index;

    @BeforeEach
    void setUp() {
        index = new InvertedIndex();
        index.addDocument(0, List.of("java", "code", "java"));
        index.addDocument(1, List.of("python", "code"));
        index.addDocument(2, List.of("java", "coffee"));
    }

    @Test
    void postingsRecordTermFrequency() {
        assertEquals(List.of(new Posting(0, 2), new Posting(2, 1)), index.getPostings("java"));
    }

    @Test
    void postingListsAreSortedByDocId() {
        assertEquals(List.of(0, 1), index.getPostings("code").stream().map(Posting::docId).toList());
    }

    @Test
    void unknownTermHasEmptyPostings() {
        assertTrue(index.getPostings("rust").isEmpty());
    }

    @Test
    void docFreqCountsDocumentsNotOccurrences() {
        assertEquals(2, index.docFreq("java"));
        assertEquals(0, index.docFreq("rust"));
    }

    @Test
    void documentCountIsNumberOfIndexedDocs() {
        assertEquals(3, index.documentCount());
    }

    @Test
    void searchAndIntersectsPostingLists() {
        assertEquals(List.of(0), index.searchAnd(List.of("java", "code")));
        assertEquals(List.of(0, 2), index.searchAnd(List.of("java")));
    }

    @Test
    void searchAndWithMissingTermIsEmpty() {
        assertTrue(index.searchAnd(List.of("java", "rust")).isEmpty());
    }

    @Test
    void searchAndWithNoTermsIsEmpty() {
        assertTrue(index.searchAnd(List.of()).isEmpty());
    }
}
