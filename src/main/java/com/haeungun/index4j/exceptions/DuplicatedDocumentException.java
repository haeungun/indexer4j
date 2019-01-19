package com.haeungun.index4j.exceptions;

public class DuplicatedDocumentException extends RuntimeException {

    private static final String errorMessage = "Duplicated document key {docId=%s}";

    public DuplicatedDocumentException(String docId) {
        super(String.format(errorMessage, docId));
    }
}
