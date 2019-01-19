package com.haeungun.index4j.exceptions;

public class UndefinedDocumentIdException extends Exception {

    private static final String errMsg = "Document must have a documentID field defined";

    public UndefinedDocumentIdException() {
        super(errMsg);
    }
}
