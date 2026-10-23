package search;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Holds the raw documents. A document's id is its position in the list,
 * so ids are 0, 1, 2, ... in the order they were added. Your inverted index
 * will rely on that (posting lists stay sorted by docId for free).
 */
public class DocumentStore {

    private final List<String> titles = new ArrayList<>();
    private final List<String> texts = new ArrayList<>();

    public int add(String title, String text) {
        titles.add(title);
        texts.add(text);
        return texts.size() - 1;
    }

    public String getText(int docId) {
        return texts.get(docId);
    }

    public String getTitle(int docId) {
        return titles.get(docId);
    }

    public int size() {
        return texts.size();
    }

    /** Loads every regular file under a directory (recursively) as one document each. */
    public static DocumentStore loadFromDirectory(Path dir) throws IOException {
        DocumentStore store = new DocumentStore();
        try (Stream<Path> paths = Files.walk(dir)) {
            List<Path> files = paths.filter(Files::isRegularFile).sorted().toList();
            for (Path file : files) {
                // Some datasets (like 20 Newsgroups) aren't clean UTF-8, so read as ISO-8859-1.
                String text = Files.readString(file, StandardCharsets.ISO_8859_1);
                store.add(dir.relativize(file).toString(), text);
            }
        }
        return store;
    }

    /** Loads a single file where each non-empty line is its own document. */
    public static DocumentStore loadFromLines(Path file) throws IOException {
        DocumentStore store = new DocumentStore();
        int line = 0;
        for (String text : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            line++;
            if (!text.isBlank()) {
                store.add(file.getFileName() + ":" + line, text);
            }
        }
        return store;
    }
}
