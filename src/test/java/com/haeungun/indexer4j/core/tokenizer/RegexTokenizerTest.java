package com.haeungun.indexer4j.core.tokenizer;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RegexTokenizerTest {

    @Test
    public void tokenizing_test() {
        RegexTokenizer tokenizer = new RegexTokenizer("\\W+");

        List<String> actual = tokenizer.tokenizing("test0 test1 test2");

        assertEquals("test0", actual.get(0));
        assertEquals("test1", actual.get(1));
        assertEquals("test2", actual.get(2));
    }
}