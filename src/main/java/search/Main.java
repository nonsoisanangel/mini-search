package search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Path path = Path.of(args.length > 0 ? args[0] : "data/sample");

        DocumentStore store = Files.isDirectory(path)
                ? DocumentStore.loadFromDirectory(path)
                : DocumentStore.loadFromLines(path);
        Tokenizer tokenizer = new Tokenizer();
        NaiveSearcher naive = new NaiveSearcher(store, tokenizer);

        long start = System.nanoTime();
        InvertedIndex index = new InvertedIndex();
        Trie trie = new Trie();
        for (int id = 0; id < store.size(); id++) {
            List<String> tokens = tokenizer.tokenize(store.getText(id));
            index.addDocument(id, tokens);
            for (String t : tokens) {
                trie.insert(t);
            }
        }
        Ranker ranker = new Ranker(index);
        double buildMs = (System.nanoTime() - start) / 1e6;

        System.out.printf("Loaded %d documents, built index in %.1f ms%n", store.size(), buildMs);
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
            List<String> terms = tokenizer.tokenize(query);

            // Time the same AND search both ways
            long t0 = System.nanoTime();
            List<Integer> naiveHits = naive.search(query);
            double naiveMs = (System.nanoTime() - t0) / 1e6;

            long t1 = System.nanoTime();
            List<Integer> indexHits = index.searchAnd(terms);
            double indexMs = (System.nanoTime() - t1) / 1e6;

            System.out.printf("%d match(es). Naive scan: %.3f ms | Index: %.3f ms%n",
                    indexHits.size(), naiveMs, indexMs);

            // Ranked results (this scores every doc containing ANY query term)
            for (Ranker.ScoredDoc r : ranker.topK(terms, 5)) {
                System.out.printf("  %.2f  %s%n", r.score(), store.getTitle(r.docId()));
            }

            // Autocomplete on the last word typed
            String[] words = query.toLowerCase().split("\\s+");
            String lastWord = words[words.length - 1];
            System.out.println("  Suggestions for \"" + lastWord + "\": " + trie.suggest(lastWord, 5));
        }
    }
}