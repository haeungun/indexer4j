/*
 * Copyright (C) 2019 The Indexer4j Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haeungun.indexer4j.core;

import com.haeungun.indexer4j.DocumentMeta;
import com.haeungun.indexer4j.annotation.*;
import com.haeungun.indexer4j.core.tokenizer.Tokenizer;
import com.haeungun.indexer4j.exceptions.UndefinedDocumentIdException;

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