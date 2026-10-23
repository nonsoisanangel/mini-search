package search;

/** One entry in a posting list: a document and how many times the term appears in it. */
public record Posting(int docId, int tf) {}
