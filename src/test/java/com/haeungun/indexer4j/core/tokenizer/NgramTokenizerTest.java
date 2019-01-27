package com.haeungun.indexer4j.core.tokenizer;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NgramTokenizerTest {

    @Test
    public void tokenizing_test() {
        NgramTokenizer tokenizer = new NgramTokenizer(2);
        List<String> actual = tokenizer.tokenizing("hello");

        assertEquals("he", actual.get(0));
        assertEquals("el", actual.get(1));
        assertEquals("ll", actual.get(2));
        assertEquals("lo", actual.get(3));
    }

    @Test
    public void tokenizing2_test() {
        NgramTokenizer tokenizer = new NgramTokenizer(3);
        List<String> actual = tokenizer.tokenizing("hello world");

        assertEquals("hel", actual.get(0));
        assertEquals("ell", actual.get(1));
        assertEquals("llo", actual.get(2));

        assertEquals("wor", actual.get(3));
        assertEquals("orl", actual.get(4));
        assertEquals("rld", actual.get(5));
    }

}