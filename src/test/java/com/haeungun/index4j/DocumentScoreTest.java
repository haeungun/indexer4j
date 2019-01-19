package com.haeungun.index4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentScoreTest {

    @Test
    public void compareTo_test() {
        DocumentScore score = new DocumentScore("doc1", 2.0);
        DocumentScore score2 = new DocumentScore("doc2", 1.0);

        assertEquals(1, score.compareTo(score2));
    }

    @Test
    public void toString_test() {
        DocumentScore score = new DocumentScore("doc1", 2.0);

        assertEquals("DocumentScore{docId=doc1, score=2.0}", score.toString());
    }
}