/*
 * Copyright (C) 2019 The Indexer4j Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haeungun.indexer4j;

import com.haeungun.indexer4j.core.DocumentExtractor;
import com.haeungun.indexer4j.core.relevance.BM25Ranker;
import com.haeungun.indexer4j.core.tokenizer.Tokenizer;
import com.haeungun.indexer4j.core.tokenizer.RegexTokenizer;
import com.haeungun.indexer4j.exceptions.DuplicatedDocumentException;
import com.haeungun.indexer4j.exceptions.UndefinedDocumentIdException;
import com.haeungun.indexer4j.core.relevance.RelevanceRanker;
import com.haeungun.indexer4j.utils.Serializer;

import java.util.*;
import java.util.stream.Collectors;

public class Indexer<T> {

    private Map<String, Map<Object, Double>> index; // {term, {docId, score}}
    private Map<Object, DocumentMeta> documents;

    private DocumentExtractor<T> extractor;
    private Tokenizer tokenizer;
    private RelevanceRanker ranker;
    private Serializer<Map<String, Map<Object, Double>>> serializer;

    /**
     * Constructor to create a Indexer object
     * by using a default options(BM25, RegexTokenizer)
     */
    public Indexer() {
        this(new BM25Ranker(), new RegexTokenizer("\\W+"));
    }

    /**
     * Constructor to create a Indexer object with default tokenizer
     * @param ranker for ranking algorithm(BM25, TFIDF, etc..)
     */
    public Indexer(RelevanceRanker ranker) {
        this(ranker, new RegexTokenizer("\\W+"));
    }

    /**
     * Constructor to create a Indexer object with default ranker
     * @param tokenizer for term and document
     */
    public Indexer(Tokenizer tokenizer) {
        this(new BM25Ranker(), tokenizer);
    }

    /**
     * Constructor to create a Indexer object
     * @param ranker for ranking algorithm(BM25, TFIDF, etc..)
     * @param tokenizer for term and document
     */
    public Indexer(RelevanceRanker ranker, Tokenizer tokenizer) {
        this.documents = new HashMap<>();
        this.serializer = new Serializer<>();
        this.ranker = ranker;
        this.extractor = new DocumentExtractor<>(tokenizer);
        this.tokenizer = tokenizer;

    }

    /**
     * Building a inverted index
     */
    public void build() {
        this.index = new HashMap<>();

        List<List<String>> docs = this.convertDocumentsToStringList();

        for (Object docId : this.documents.keySet()) {
            DocumentMeta doc = this.documents.get(docId);

            for (String word : doc.getTokenizedWords()) {
                Map<Object, Double> scores = this.index.get(word);
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

    /**
     * Adding a doucment.
     * @param doc to add
     * @return true if success to add
     * @throws UndefinedDocumentIdException when the DocumentId is not designated
     */
    public boolean add(T doc) throws UndefinedDocumentIdException {
        boolean allowOverwrite = false;
        return this.add(doc, allowOverwrite);
    }

    /**
     * Adding a doucment.
     * @param doc doc to add
     * @param allowOverwrite true if you want to allow overwrite document for same document id
     * @return true if success to add
     * @throws UndefinedDocumentIdException when the DocumentId is not designated
     */
    public boolean add(T doc, boolean allowOverwrite) throws UndefinedDocumentIdException {
        if (!this.extractor.isDocument(doc)) return false;

        DocumentMeta meta;
        try {
            meta = this.extractor.extractDocumentMeta(doc);
        } catch (IllegalAccessException e) {
            // TODO(haeungun)
            return false;
        }

        if (!allowOverwrite && this.documents.containsKey(meta.getDocId()))
            throw new DuplicatedDocumentException(meta.getDocId().toString());

        this.documents.put(meta.getDocId(), meta);
        return true;
    }

    /**
     * Saving a index into local disk.
     * @param fileName to save a index into the local disk
     * @return true if success to save
     */
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

    /**
     * Loading a index from the designate file path.
     * @param fileName to load a index from the local disk
     * @return true if success to load
     */
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

    /**
     * Retrieve search results from a inverted index.
     * @param query to search documents
     * @return the list of SearchResult in default topK
     */
    public List<SearchResult> search(String query) {
        int defaultTopK = 100;
        return this.search(query, defaultTopK);
    }

    /**
     * Retrieve search results from a inverted index.
     * @param query to search documents
     * @param topK the number of maximum documents
     * @return the list of SearchResults in topK
     */
    public List<SearchResult> search(String query, int topK) {
        List<String> queries = this.tokenizer.tokenizing(query);
        Map<Object, Double> results = new HashMap<>(); // {docId, score}
        for (String q : queries) {
            if (index.containsKey(q)) {
                Map<Object, Double> scores = this.index.get(q);
                for (Object docId : scores.keySet()) {
                    double score = results.getOrDefault(docId, 0.0);
                    score += scores.get(docId);
                    results.put(docId, score);
                }
            }
        }

        return results.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .map(e -> new SearchResult(e.getKey(), e.getValue()))
                .limit(topK)
                .collect(Collectors.toList());
    }

    private List<List<String>> convertDocumentsToStringList() {
        List<List<String>> docs = new ArrayList<>();
        for (Object docId : this.documents.keySet()) {
            docs.add(this.documents.get(docId).getTokenizedWords());
        }
        return docs;
    }
}