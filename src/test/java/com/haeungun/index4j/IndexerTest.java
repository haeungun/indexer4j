package com.haeungun.index4j;

import com.haeungun.index4j.core.relevance.Relevance;
import com.haeungun.index4j.example.ExampleDocument;
import com.haeungun.index4j.example.WrongDocument;
import com.haeungun.index4j.exceptions.UndefinedDocumentIdException;
import com.haeungun.index4j.exceptions.UnsupportedRelevanceException;
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
            new ExampleDocument("doc2", "Third Document", "Lorem is hello java python"),
            new ExampleDocument("doc3", "Second Document", "Lorem ipsum dolor"),
            new ExampleDocument("doc4", "Forth Document", "Lorem")
    );

    @Test
    public void addDocument_test() throws UndefinedDocumentIdException, UnsupportedRelevanceException {
        Indexer<WrongDocument> index = new Indexer<>();
        assertFalse(index.add(new WrongDocument()));
    }

    @Test
    public void BM25_test() throws UndefinedDocumentIdException, UnsupportedRelevanceException {
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
    public void TFIDF_test() throws UnsupportedRelevanceException, UndefinedDocumentIdException {
        Indexer<ExampleDocument> index = new Indexer<>(Relevance.TFIDF);
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

    @Test
    public void save_test() throws UnsupportedRelevanceException, UndefinedDocumentIdException {
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
    }

    @After
    public void removeFile() {
        File file = new File(this.filePath);

        if (file.exists()) {
            file.delete();
        }
    }
}