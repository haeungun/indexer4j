package com.haeungun.index4j.core;

import com.haeungun.index4j.DocumentMeta;
import com.haeungun.index4j.annotation.*;
import com.haeungun.index4j.core.tokenizer.Tokenizer;
import com.haeungun.index4j.exceptions.UndefinedDocumentIdException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DocumentExtractor<T> {

    private final Tokenizer tokenizer;

    private Field[] fields;

    public DocumentExtractor(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public DocumentMeta extractDocumentMeta(T doc) throws UndefinedDocumentIdException, IllegalAccessException {
        String docId = this.extractDocumentId(doc);
        List<String> fieldValues = this.extractDocumentFieldValues(doc);

        List<String> tokenizedValues = new ArrayList<>();
        for (String fieldValue : fieldValues) {
            List<String> tokens = this.tokenizer.tokenizing(fieldValue);
            if (tokens != null) tokenizedValues.addAll(tokens);
        }

        return new DocumentMeta(docId, tokenizedValues);
    }

    private String extractDocumentId(T doc) throws IllegalAccessException, UndefinedDocumentIdException {
        if (this.fields == null) this.setFieldsByDocument(doc);

        for (Field field : this.fields) {
            if (field.isAnnotationPresent(DocumentId.class)) {
                field.setAccessible(true);
                return (String) field.get(doc);
            }
        }

        throw new UndefinedDocumentIdException();
    }

    private List<String> extractDocumentFieldValues(T doc) throws IllegalAccessException {
        if (this.fields == null) this.setFieldsByDocument(doc);

        List<String> values = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DocumentField.class)) {
                field.setAccessible(true);
                values.add((String) field.get(doc));
            }
        }

        return values;
    }

    public boolean isDocument(T doc) {
        return doc.getClass().isAnnotationPresent(Document.class);
    }

    private void setFieldsByDocument(T doc) {
        this.fields = doc.getClass().getDeclaredFields();
    }
}