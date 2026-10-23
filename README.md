# Mini Search Engine

A small search engine written in Java from scratch: inverted index, TF-IDF ranking,
and trie-based autocomplete, benchmarked against a naive full scan.

> Status: Stage 1 done (tokenizer, document store, naive baseline). Stages 2-7 in progress.

## Run it

Requires Java 25+ and Maven.

```bash
mvn test                                              # run unit tests
mvn -q compile exec:java -Dexec.args="data/sample"    # interactive search
```

The argument is a directory (one document per file) or a text file (one document per line).

## Get a bigger dataset

Download the 20 Newsgroups dataset (about 20K posts), unpack it to `data/20news/`, then:

```bash
mvn -q compile exec:java -Dexec.args="data/20news"
```

## Architecture

TODO: add a diagram (documents -> Tokenizer -> InvertedIndex -> Ranker -> results, and Tokenizer -> Trie -> suggestions).

## Complexity

| Operation | Naive | This project |
|---|---|---|
| Single-term query | O(N * L) | O(posting list length) |
| Two-term AND | O(N * L) | O(m + n) merge |
| Top-k results | O(n log n) | O(n log k) heap |
| Autocomplete | O(W) | O(prefix length + subtree) |

## Benchmarks
| Documents | Naive (ms/query) | Index (ms/query) | Ranked top-10 (ms/query) | Speedup |
|---|---|---|---|---|
| 1,000 | 36.692 | 0.0546 | 0.2566 | 673x |
| 5,000 | 187.364 | 0.1548 | 0.3667 | 1,211x |
| 10,000 | 351.318 | 0.1455 | 0.1700 | 2,415x |
| 18,846 | 646.818 | 0.1217 | 0.2851 | 5,313x |
Averaged over 50 random 1-2 word queries on the 20 Newsgroups dataset, after a JVM warm-up.
Index timings are sub-millisecond and noisy, so treat the exact speedup as approximate.

## What I'd improve next

- Stemming (so "running" matches "run")
- Phrase search
- Index compression and saving the index to disk
- Sharding for much larger corpora
