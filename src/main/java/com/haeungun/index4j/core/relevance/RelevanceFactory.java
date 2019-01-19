package com.haeungun.index4j.core.relevance;

import com.haeungun.index4j.exceptions.UnsupportedRelevanceException;

public class RelevanceFactory {
	
    public static RelevanceRanker createRanker(Relevance relevance) throws UnsupportedRelevanceException {
        switch (relevance) {
            case TFIDF:
                return new TFIDFRanker();
            case BM25:
                return new BM25Ranker();
            default:
                throw new UnsupportedRelevanceException(relevance.name());
        }
    }
}
