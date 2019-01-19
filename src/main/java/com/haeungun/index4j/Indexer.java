package com.haeungun.index4j;

import com.haeungun.index4j.core.DocumentExtractor;
import com.haeungun.index4j.core.tokenizer.Tokenizer;
import com.haeungun.index4j.core.tokenizer.RegexTokenizer;
import com.haeungun.index4j.exceptions.UndefinedDocumentIdException;
import com.haeungun.index4j.exceptions.UnsupportedRelevanceException;
import com.haeungun.index4j.core.relevance.Relevance;
import com.haeungun.index4j.core.relevance.RelevanceFactory;
import com.haeungun.index4j.core.relevance.RelevanceRanker;
import com.haeungun.index4j.utils.Serializer;

import java.util.*;
import java.util.stream.Collectors;

public class Indexer<T> {

    private Map<String, Map<String, Double>> index; // {term, {docId, score}}
    private Map<String, DocumentMeta> documents;

    private DocumentExtractor<T> extractor;
    private Tokenizer tokenizer;
    private RelevanceRanker ranker;
    private Serializer<Map<String, Map<String, Double>>> serializer;

    public Indexer() throws UnsupportedRelevanceException {
        this(Relevance.BM25, new RegexTokenizer("\\W+"));
    }

    public Indexer(Relevance relevance) throws UnsupportedRelevanceException {
        this(relevance, new RegexTokenizer("\\W+"));
    }

    public Indexer(Relevance relevance, Tokenizer tokenizer) throws UnsupportedRelevanceException {
        this.documents = new HashMap<>();
        this.serializer = new Serializer<>();
        this.ranker = RelevanceFactory.createRanker(relevance);
        this.extractor = new DocumentExtractor<>(tokenizer);
        this.tokenizer = tokenizer;

    }

    public void build() {
        this.index = new HashMap<>();

        List<List<String>> docs = this.convertDocumentsToStringList();

        for (String docId : this.documents.keySet()) {
            DocumentMeta doc = this.documents.get(docId);

            for (String word : doc.getTokenizedWords()) {
                Map<String, Double> scores = this.index.get(word);
                if (scores == null) {
                    scores = new HashMap<>();
                    this.index.put(word, scores);
                }

                double score = scores.getOrDefault(docId, 0.0);
                score += this.ranker.rank(word, doc.getTokenizedWords(), docs);
                scores.put(docId, score);
            }
        }
    }

    public boolean add(T doc, boolean allowDuplicated) throws UndefinedDocumentIdException {
        if (!this.extractor.isDocument(doc)) return false;

        DocumentMeta meta;
        try {
            meta = this.extractor.extractDocumentMeta(doc);
        } catch (IllegalAccessException e) {
            // TODO(haeungun)
            return false;
        }

        if (!allowDuplicated && this.documents.containsKey(meta.getDocId())) return false;

        this.documents.put(meta.getDocId(), meta);
        return true;
    }

    public boolean save(String fileName) {
        assert this.index != null;
        try {
            this.serializer.serializing(fileName, this.index);
        } catch (Exception e) {
            // TODO(haeungun)
            return false;
        }
        return true;
    }

    public boolean load(String fileName) {
        try {
            this.index = this.serializer.deserializing(fileName);
        } catch (Exception e) {
            // TODO(haeungun)
            return false;
        }
        assert this.index != null;
        return true;
    }

    public List<SearchResult> search(String query) {
        List<String> queries = this.tokenizer.tokenizing(query);
        Map<String, Double> results = new HashMap<>(); // {docId, score}
        for (String q : queries) {
            if (index.containsKey(q)) {
                Map<String, Double> scores = this.index.get(q);
                for (String docId : scores.keySet()) {
                    double score = results.getOrDefault(docId, 0.0);
                    score += scores.get(docId);
                    results.put(docId, score);
                }
            }
        }

        return results.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .map(e -> new SearchResult(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private List<List<String>> convertDocumentsToStringList() {
        List<List<String>> docs = new ArrayList<>();
        for (String docId : this.documents.keySet()) {
            docs.add(this.documents.get(docId).getTokenizedWords());
        }
        return docs;
    }
}