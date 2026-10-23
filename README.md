# Mini Search Engine

A small search engine written in Java from scratch. It builds an inverted index, ranks results with TF-IDF, and suggests completions with a trie. I benchmarked it against a naive full scan on the 20 Newsgroups dataset.

## Features

- **Inverted index** with posting lists sorted by document id
- **AND queries** using a two-pointer merge of posting lists (O(m + n))
- **TF-IDF ranking** with a min-heap that keeps only the top k results (O(n log k))
- **Trie autocomplete** that returns the most frequent completions for a prefix
- **Naive baseline** (scan every document) used to measure the speedup
- **Unit tests** (JUnit 5) for the tokenizer, index, ranker, and trie

## Results

Average over 50 random 1-2 word queries on the 20 Newsgroups dataset, after a JVM warm-up:

| Documents | Naive (ms/query) | Index (ms/query) | Ranked top-10 (ms/query) |
|---|---|---|---|
| 1,000 | 36.7 | 0.055 | 0.257 |
| 5,000 | 187.4 | 0.155 | 0.367 |
| 10,000 | 351.3 | 0.146 | 0.170 |
| 18,846 | 646.8 | 0.122 | 0.285 |

The naive scan slows down as the corpus grows. The index stays near a tenth of a millisecond because it only touches documents that contain the query words. The index timings are sub-millisecond and noisy, so treat exact ratios as approximate.

## How it works

```
documents --> Tokenizer --> InvertedIndex --> Ranker --> top-k results
                  |
                  +-------> Trie ------------------------> suggestions
```

1. **Tokenizer** lowercases text, splits on non-alphanumeric characters, and drops stopwords.
2. **InvertedIndex** maps each term to a list of (docId, term frequency) entries. Documents are added in order, so each list is already sorted. An AND query starts with the rarest term and intersects the lists with two pointers.
3. **Ranker** scores each matching document as the sum of `tf * log(N / df)` over the query terms, so rare words count for more. A min-heap of size k keeps only the best results.
4. **Trie** stores every word with a count. To autocomplete, it walks down the prefix and returns the most frequent words below that node.

## Complexity

| Operation | Naive | This project |
|---|---|---|
| Single-term query | O(N * L) | O(length of posting list) |
| Two-term AND | O(N * L) | O(m + n) merge |
| Top-k results | O(n log n) sort | O(n log k) heap |
| Autocomplete | O(W) scan of all words | O(prefix length + words below it) |

N = documents, L = average document length, W = vocabulary size.

## Run it

Requires Java 17+ and Maven.

```bash
mvn test                                             # run unit tests
mvn -q compile exec:java -Dexec.args="data/sample"   # interactive search on a small sample
```

To use the full dataset, download 20 Newsgroups and unpack it into `data/20news/`:

```bash
mkdir -p data/20news
curl -L -o /tmp/20news.tar.gz http://qwone.com/~jason/20Newsgroups/20news-bydate.tar.gz
tar -xzf /tmp/20news.tar.gz -C data/20news
mvn -q compile exec:java -Dexec.args="data/20news"
```

Run the benchmark:

```bash
mvn -q compile exec:java -Dexec.mainClass=search.Benchmark -Dexec.args="data/20news"
```

## Project layout

```
src/main/java/search/
  Tokenizer.java       text -> terms
  DocumentStore.java   loads documents
  NaiveSearcher.java   baseline full scan
  InvertedIndex.java   index + AND search
  Ranker.java          TF-IDF + top-k heap
  Trie.java            prefix autocomplete
  Benchmark.java       timing harness
  Main.java            command-line interface
src/test/java/search/  unit tests
```

## AI usage

I used AI tools while building this. `AI_NOTES.md` logs where they helped, what they got wrong, and how I caught and fixed it.

## What I'd improve next

- Stemming, so "running" matches "run"
- Phrase search
- Index compression and saving the index to disk
- BM25 ranking, compared against TF-IDF
- Sharding for much larger corpora