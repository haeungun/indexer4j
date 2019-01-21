package com.haeungun.indexer4j.example;

import com.haeungun.indexer4j.annotation.Document;
import com.haeungun.indexer4j.annotation.DocumentField;
import com.haeungun.indexer4j.annotation.DocumentId;

@Document
public class IntegerDocument {

    @DocumentId
    private final Integer docId;

    @DocumentField
    private final String field;

    public IntegerDocument(Integer docId, String field) {
        this.docId = docId;
        this.field = field;
    }

    public Integer getDocId() {
        return docId;
    }

    public String getField() {
        return field;
    }
}
