package search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Command-line interface.
 *
 * Run:  mvn -q compile exec:java -Dexec.args="data/sample"
 *
 * The argument is either a directory (one doc per file) or a single text file
 * (one doc per line).
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Path path = Path.of(args.length > 0 ? args[0] : "data/sample");

        long start = System.nanoTime();
        DocumentStore store = Files.isDirectory(path)
                ? DocumentStore.loadFromDirectory(path)
                : DocumentStore.loadFromLines(path);
        Tokenizer tokenizer = new Tokenizer();
        NaiveSearcher naive = new NaiveSearcher(store, tokenizer);
        double loadMs = (System.nanoTime() - start) / 1e6;

        System.out.printf("Loaded %d documents in %.1f ms%n", store.size(), loadMs);
        System.out.println("Type a query (or 'quit').");

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            if (!in.hasNextLine()) {
                break;
            }
            String query = in.nextLine().trim();
            if (query.equalsIgnoreCase("quit")) {
                break;
            }
            if (query.isEmpty()) {
                continue;
            }

            long t0 = System.nanoTime();
            List<Integer> hits = naive.search(query);
            double ms = (System.nanoTime() - t0) / 1e6;

            System.out.printf("%d result(s) in %.2f ms (naive scan)%n", hits.size(), ms);
            for (int i = 0; i < Math.min(10, hits.size()); i++) {
                int id = hits.get(i);
                System.out.printf("  [%d] %s%n", id, store.getTitle(id));
            }
            // TODO Stage 2/3: switch to InvertedIndex + Ranker and print scores.
            // TODO Stage 4: when the query ends mid-word, print Trie suggestions.
        }
    }
}
