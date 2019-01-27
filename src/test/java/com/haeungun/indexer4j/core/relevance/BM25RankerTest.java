package com.haeungun.indexer4j.core.relevance;

import org.junit.Test;

public class BM25RankerTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionK1_test() {
        new BM25Ranker(-1, .7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionB_test() {
        new BM25Ranker(3, 2);
    }

}