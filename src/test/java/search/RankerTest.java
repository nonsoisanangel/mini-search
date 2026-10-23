package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class RankerTest {

    private Ranker ranker;

    @BeforeEach
    void setUp() {
        InvertedIndex index = new InvertedIndex();
        index.addDocument(0, List.of("java", "java", "java", "code"));
        index.addDocument(1, List.of("java", "code"));
        index.addDocument(2, List.of("coffee", "code"));
        index.addDocument(3, List.of("tea", "code"));
        ranker = new Ranker(index);
    }

    @Test
    void higherTermFrequencyRanksHigher() {
        List<Ranker.ScoredDoc> top = ranker.topK(List.of("java"), 10);
        assertEquals(0, top.get(0).docId());
        assertEquals(1, top.get(1).docId());
    }

    @Test
    void resultsAreLimitedToK() {
        assertEquals(1, ranker.topK(List.of("code"), 1).size());
    }

    @Test
    void scoresAreSortedDescending() {
        List<Ranker.ScoredDoc> top = ranker.topK(List.of("java", "code"), 10);
        for (int i = 1; i < top.size(); i++) {
            assertTrue(top.get(i - 1).score() >= top.get(i).score());
        }
    }

    @Test
    void rareTermsMatterMoreThanCommonOnes() {
        // "code" is in every doc (idf = 0), "coffee" is rare, so the coffee doc wins.
        List<Ranker.ScoredDoc> top = ranker.topK(List.of("code", "coffee"), 10);
        assertEquals(2, top.get(0).docId());
    }
}
