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

package com.haeungun.indexer4j;

import com.haeungun.indexer4j.core.relevance.TFIDFRanker;
import com.haeungun.indexer4j.example.ExampleDocument;
import com.haeungun.indexer4j.example.IntegerDocument;
import com.haeungun.indexer4j.example.WrongDocument;
import com.haeungun.indexer4j.exceptions.DuplicatedDocumentException;
import com.haeungun.indexer4j.exceptions.UndefinedDocumentIdException;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class IndexerTest {

    private final String filePath = "./index.test";

    private List<ExampleDocument> documents = Arrays.asList(
            new ExampleDocument("doc1", "First Document", "Lorem Lorem Lorem Lorem Lorem"),
            new ExampleDocument("doc2", "Second Document", "Lorem is hello java python"),
            new ExampleDocument("doc3", "Third Document", "Lorem ipsum dolor"),
            new ExampleDocument("doc4", "Forth Document", "Lorem")
    );

    private List<IntegerDocument> integerDocuments = Arrays.asList(
            new IntegerDocument(1, "First Document"),
            new IntegerDocument(2, "Second Document"),
            new IntegerDocument(3, "Third Document"),
            new IntegerDocument(4, "Forth Document")
    );

    @Test
    public void addDocument_test() throws UndefinedDocumentIdException {
        Indexer<WrongDocument> index = new Indexer<>();
        assertFalse(index.add(new WrongDocument()));
    }

    @Test
    public void BM25_test() throws UndefinedDocumentIdException {
        Indexer<ExampleDocument> index = new Indexer<>();
        for (ExampleDocument document : this.documents) {
            index.add(document);
        }

        index.build();

        String query = "First";
        List<SearchResult> result = index.search(query);

        assertEquals(1, result.size());
        assertEquals("doc1", result.get(0).getDocId());

        String query2 = "Lorem ipsum";
        List<SearchResult> result2 = index.search(query2);

        assertEquals(4, result2.size());
        assertEquals("doc3", result2.get(0).getDocId());
        assertEquals("doc1", result2.get(1).getDocId());
        assertEquals("doc4", result2.get(2).getDocId());
        assertEquals("doc2", result2.get(3).getDocId());
    }

    @Test
    public void TFIDF_test() throws UndefinedDocumentIdException {
        Indexer<ExampleDocument> index = new Indexer<>(new TFIDFRanker());
        for (ExampleDocument document : this.documents) {
            index.add(document);
        }

        index.build();

        String query = "First";
        List<SearchResult> result = index.search(query);

        assertEquals(1, result.size());
        assertEquals("doc1", result.get(0).getDocId());

        String query2 = "Lorem ipsum";
        List<SearchResult> result2 = index.search(query2);

        assertEquals(4, result2.size());
        assertEquals("doc1", result2.get(0).getDocId());
        assertEquals("doc3", result2.get(1).getDocId());
        assertEquals("doc4", result2.get(2).getDocId());
        assertEquals("doc2", result2.get(3).getDocId());
    }

    @Test(expected = DuplicatedDocumentException.class)
    public void addDuplicatedDocumentException_test() throws UndefinedDocumentIdException {
        Indexer<ExampleDocument> index = new Indexer<>();
        index.add(this.documents.get(0));
        index.add(this.documents.get(0));
    }

    @Test
    public void allowDuplicatedDocumentException_test() throws UndefinedDocumentIdException {
        Indexer<ExampleDocument> index = new Indexer<>();
        index.add(this.documents.get(0), true);
        index.add(this.documents.get(0), true);

        index.build();

        List<SearchResult> results = index.search("First");

        assertEquals(1, results.size());
        assertEquals("doc1", results.get(0).getDocId());
    }


    @Test
    public void save_test() throws UndefinedDocumentIdException {
        Indexer<ExampleDocument> index = new Indexer<>();
        for (ExampleDocument document : this.documents) {
            index.add(document);
        }

        index.build();

        assertTrue(index.save(this.filePath));

        File file = new File(this.filePath);
        assertTrue(file.exists());

        assertTrue(index.load(this.filePath));

        String query = "First";
        List<SearchResult> result = index.search(query);

        assertEquals(1, result.size());
        assertEquals("doc1", result.get(0).getDocId());

        String query2 = "Lorem ipsum";
        List<SearchResult> result2 = index.search(query2);

        assertEquals(4, result2.size());
        assertEquals("doc3", result2.get(0).getDocId());
        assertEquals("doc1", result2.get(1).getDocId());
        assertEquals("doc4", result2.get(2).getDocId());
        assertEquals("doc2", result2.get(3).getDocId());

        List<SearchResult> result3 = index.search(query2, 2);
        assertEquals(2, result3.size());
        assertEquals("doc3", result3.get(0).getDocId());
        assertEquals("doc1", result3.get(1).getDocId());
    }

    @Test
    public void integerDocument_test() throws UndefinedDocumentIdException {
        Indexer<IntegerDocument> index = new Indexer<>();
        for (IntegerDocument document : this.integerDocuments) {
            index.add(document);
        }

        index.build();

        String query = "First";
        List<SearchResult> result = index.search(query);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getDocId());
    }

    @After
    public void removeFile() {
        File file = new File(this.filePath);

        if (file.exists()) {
            file.delete();
        }
    }
}