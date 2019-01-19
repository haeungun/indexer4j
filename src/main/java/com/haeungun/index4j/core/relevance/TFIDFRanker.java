package com.haeungun.index4j.core.relevance;

import java.util.List;

public class TFIDFRanker implements RelevanceRanker {

    private double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
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
        // 1 + n is written because the denominator is 0 if the term does not exist in the entire corpus
        return Math.log(docs.size() / 1 + n);
    }

    @Override
    public double rank(String term, List<String> doc, List<List<String>> docs) {
        return tf(doc, term) * idf(docs, term);
    }

}
