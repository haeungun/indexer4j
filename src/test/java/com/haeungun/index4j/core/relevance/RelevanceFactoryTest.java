package com.haeungun.index4j.core.relevance;

import static org.junit.Assert.*;

import com.haeungun.index4j.exceptions.UnsupportedRelevanceException;
import org.junit.Test;

public class RelevanceFactoryTest {

    @Test
    public void createTFIDFRanker_test() throws UnsupportedRelevanceException {
        RelevanceRanker ranker = RelevanceFactory.createRanker(Relevance.TFIDF);
        assertTrue(ranker instanceof TFIDFRanker);
    }

    @Test
    public void testBM25Ranker_test() throws UnsupportedRelevanceException {
        RelevanceRanker ranker = RelevanceFactory.createRanker(Relevance.BM25);
        assertTrue(ranker instanceof BM25Ranker);
    }

}
