package com.haeungun.index4j.core.tokenizer;

import java.util.List;

public interface Tokenizer {

    /**
     * Parses the document and returns it as a term list
     * @param str to tokenized
     * @return split term list
     */
    List<String> tokenizing(String str);
}
