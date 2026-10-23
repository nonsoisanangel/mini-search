package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class TokenizerTest {

    private final Tokenizer tokenizer = new Tokenizer();

    @Test
    void lowercasesAndSplitsOnPunctuation() {
        assertEquals(List.of("hello", "world"), tokenizer.tokenize("Hello, WORLD!"));
    }

    @Test
    void dropsStopwords() {
        assertEquals(List.of("cat", "mat"), tokenizer.tokenize("The cat is on the mat"));
    }

    @Test
    void keepsDuplicatesBecauseTermFrequencyNeedsThem() {
        assertEquals(List.of("java", "java", "code"), tokenizer.tokenize("java java code"));
    }

    @Test
    void keepsDigits() {
        assertEquals(List.of("java", "17"), tokenizer.tokenize("Java 17"));
    }

    @Test
    void handlesEmptyBlankAndNull() {
        assertTrue(tokenizer.tokenize("").isEmpty());
        assertTrue(tokenizer.tokenize("   \t\n").isEmpty());
        assertTrue(tokenizer.tokenize(null).isEmpty());
    }

    @Test
    void textMadeOnlyOfStopwordsGivesNoTokens() {
        assertTrue(tokenizer.tokenize("the and of").isEmpty());
    }
}
