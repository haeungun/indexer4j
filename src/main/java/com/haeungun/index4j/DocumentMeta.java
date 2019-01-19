package com.haeungun.index4j;

import java.util.List;

public class DocumentMeta {

    private final String docId;
    private final List<String> tokenizedWords;

    public DocumentMeta(String docId, List<String> tokenizedWords) {
        this.docId = docId;
        this.tokenizedWords = tokenizedWords;
    }

    public String getDocId() {
        return this.docId;
    }

    public List<String> getTokenizedWords() {
        return this.tokenizedWords;
    }

    @Override
    public String toString() {
        return "DocumentMeta{docId=" + this.docId
                + ", tokenizedWords=" + this.tokenizedWords.toString() + "}";
    }
}
