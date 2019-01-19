package com.haeungun.index4j.core.tokenizer;

import java.util.Arrays;
import java.util.List;

public class RegexTokenizer implements Tokenizer {

    private final String regex;

    public RegexTokenizer(String regex) {
        this.regex = regex;
    }

    @Override
    public List<String> tokenizing(String str) {
        return Arrays.asList(str.split(regex));
    }

}
