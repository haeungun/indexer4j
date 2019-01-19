package com.haeungun.index4j.core.relevance;

import java.util.List;

public class BM25Ranker implements RelevanceRanker {
    private static final double DEFAULT_K1 = 1.2;
    private static final double DEFAULT_B = .75;

    /**
     * The higher the value of k1, the more weight is given to tf.
     * In general, values ​​between 0 and 3 are used (cf. ES default k1 value = 1.2)
     */
    private double k1;

    /**
     * The closer b is to 1, the more weight is placed on the length of the document.
     * Requires a value between 0 and 1 (cf. ES default k1 value = .75)
     */
    private double b;

    public BM25Ranker() {
        this(DEFAULT_K1, DEFAULT_B);
    }

    public BM25Ranker(double k1, double b) {
        if (k1 < 0)
            throw new IllegalArgumentException("k1 must be positive");

        if (b < 0 || b > 1)
            throw new IllegalArgumentException("b needs to be between 0 and 1");

        this.k1 = k1;
        this.b = b;
    }

    private double termFrequency(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result;
    }

    private double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(1 + (docs.size() - n + 0.5) / (n + 0.5));
    }

    private double score(double freq, double idf, int docSize, double avgDocSize) {
        if (freq <= 0) return 0.0;
        double tf = freq * (this.k1 + 1) / (freq + this.k1 * (1 - this.b + this.b * docSize / avgDocSize));
        return tf  * idf;
    }

    private double calculateAvgDocSize(List<List<String>> docs) {
        double amountOfSize = 0;
        for (List<String> doc : docs) {
            amountOfSize += doc.size();
        }
        return amountOfSize / docs.size();
    }

    @Override
    public double rank(String term, List<String> doc, List<List<String>> docs) {
        double freq = this.termFrequency(doc, term);
        double idf = this.idf(docs, term);
        int docSize = doc.size();
        double avgDocSize = this.calculateAvgDocSize(docs);

        return this.score(freq, idf, docSize, avgDocSize);
    }
}
