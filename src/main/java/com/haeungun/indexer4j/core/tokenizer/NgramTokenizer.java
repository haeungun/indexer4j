package com.haeungun.indexer4j.core.tokenizer;

import java.util.ArrayList;
import java.util.List;

public class NgramTokenizer implements Tokenizer {

    private static final String SPLIT_REGEX = "\\W+";
    private final int n;

    public NgramTokenizer(int n) {
        this.n = n;
    }

    @Override
    public List<String> tokenizing(String str) {
        List<String> results = new ArrayList<>();

        String[] splits = str.split(SPLIT_REGEX);
        for (String split : splits) {
            results.addAll(this.generateNgram(split));
        }

        return results;
    }

    private List<String> generateNgram(String str) {
        List<String> ngram = new ArrayList<>();
        for (int i = 0; i < str.length() - n + 1; i++) {
            ngram.add(str.substring(i, i + n));
        }
        return ngram;
    }
}
