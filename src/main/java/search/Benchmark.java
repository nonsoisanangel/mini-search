

package search;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
 
/**
 * Times the naive scan against the inverted index at several corpus sizes.
 *
 * Run:  mvn -q compile exec:java -Dexec.mainClass=search.Benchmark -Dexec.args="data/20news"
 */
public class Benchmark {
 
    private static final int QUERIES = 50;
    private static final int WARMUP = 3;
    private static int sink; // keeps the JVM from optimizing the searches away
 
    public static void main(String[] args) throws IOException {
        Path path = Path.of(args.length > 0 ? args[0] : "data/20news");
        DocumentStore full = Files.isDirectory(path)
                ? DocumentStore.loadFromDirectory(path)
                : DocumentStore.loadFromLines(path);
        Tokenizer tokenizer = new Tokenizer();
        System.out.println("Loaded " + full.size() + " documents. Running " + QUERIES + " queries per size...");
 
        // Shuffle so each smaller corpus is a random sample, not one newsgroup.
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < full.size(); i++) {
            order.add(i);
        }
        Collections.shuffle(order, new Random(42));
 
        int[] sizes = {1000, 5000, 10000, full.size()};
 
        System.out.println();
        System.out.println("| Documents | Naive (ms/query) | Index (ms/query) | Ranked top-10 (ms/query) | Speedup |");
        System.out.println("|---|---|---|---|---|");
 
        int lastSize = -1;
        for (int size : sizes) {
            if (size > full.size() || size == lastSize) {
                continue;
            }
            lastSize = size;
 
            DocumentStore store = new DocumentStore();
            for (int i = 0; i < size; i++) {
                int id = order.get(i);
                store.add(full.getTitle(id), full.getText(id));
            }
 
            NaiveSearcher naive = new NaiveSearcher(store, tokenizer);
            InvertedIndex index = new InvertedIndex();
            for (int id = 0; id < store.size(); id++) {
                index.addDocument(id, tokenizer.tokenize(store.getText(id)));
            }
            Ranker ranker = new Ranker(index);
 
            List<String> queries = makeQueries(store, tokenizer, new Random(7));
            List<List<String>> termLists = new ArrayList<>();
            for (String q : queries) {
                termLists.add(tokenizer.tokenize(q));
            }
 
            // Warm-up: let the JVM compile the hot code before we time anything.
            for (int i = 0; i < WARMUP; i++) {
                sink += naive.search(queries.get(i)).size();
                sink += index.searchAnd(termLists.get(i)).size();
                sink += ranker.topK(termLists.get(i), 10).size();
            }
 
            long t = System.nanoTime();
            for (String q : queries) {
                sink += naive.search(q).size();
            }
            double naiveMs = (System.nanoTime() - t) / 1e6 / queries.size();
 
            t = System.nanoTime();
            for (List<String> terms : termLists) {
                sink += index.searchAnd(terms).size();
            }
            double indexMs = (System.nanoTime() - t) / 1e6 / queries.size();
 
            t = System.nanoTime();
            for (List<String> terms : termLists) {
                sink += ranker.topK(terms, 10).size();
            }
            double rankedMs = (System.nanoTime() - t) / 1e6 / queries.size();
 
            double speedup = naiveMs / Math.max(indexMs, 1e-6);
            System.out.printf("| %,d | %.3f | %.4f | %.4f | %,.0fx |%n",
                    size, naiveMs, indexMs, rankedMs, speedup);
        }
        System.out.println();
        System.out.println("(checksum " + sink + ")");
    }
 
    /** Builds queries from real words: pick a random doc, take 1 or 2 of its words. */
    private static List<String> makeQueries(DocumentStore store, Tokenizer tokenizer, Random rng) {
        List<String> queries = new ArrayList<>();
        while (queries.size() < QUERIES) {
            List<String> tokens = tokenizer.tokenize(store.getText(rng.nextInt(store.size())));
            if (tokens.size() < 2) {
                continue;
            }
            String first = tokens.get(rng.nextInt(tokens.size()));
            if (rng.nextBoolean()) {
                queries.add(first);
            } else {
                queries.add(first + " " + tokens.get(rng.nextInt(tokens.size())));
            }
        }
        return queries;
    }
}
 
