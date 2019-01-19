package com.haeungun.index4j.core;

import static org.junit.Assert.*;

import com.haeungun.index4j.DocumentMeta;
import com.haeungun.index4j.core.tokenizer.RegexTokenizer;
import com.haeungun.index4j.core.tokenizer.Tokenizer;
import com.haeungun.index4j.example.ExampleDocument;
import com.haeungun.index4j.example.WrongDocument;
import org.junit.Before;
import org.junit.Test;

public class DocumentExtractorTest {

    String id = "docTest";
    String title = "Title test";
    String contents = "Contents test";

    private ExampleDocument eDoc;
    private WrongDocument wDoc;

    private DocumentExtractor<ExampleDocument> eExtractor;
    private DocumentExtractor<WrongDocument> wExtractor;
    private Tokenizer tokenizer;

    @Before
    public void initDocument() {
        this.eDoc = new ExampleDocument(this.id, this.title, this.contents);
        this.wDoc = new WrongDocument();
        this.tokenizer = new RegexTokenizer("\\W+");
        this.eExtractor = new DocumentExtractor<>(this.tokenizer);
        this.wExtractor = new DocumentExtractor<>(this.tokenizer);
    }

    @Test(expected=Exception.class)
    public void extractorException_test() throws Exception {
        this.wExtractor.extractDocumentMeta(this.wDoc);
    }

    @Test
    public void extractDocumentMeta_test() throws Exception {
        DocumentMeta actual = this.eExtractor.extractDocumentMeta(this.eDoc);

        assertEquals(this.id, actual.getDocId());
        assertEquals(4, actual.getTokenizedWords().size());
        assertEquals("Title", actual.getTokenizedWords().get(0));
        assertEquals("test", actual.getTokenizedWords().get(1));
        assertEquals("Contents", actual.getTokenizedWords().get(2));
        assertEquals("test", actual.getTokenizedWords().get(3));
    }

    @Test
    public void isDocument_test() {
        assertTrue(this.eExtractor.isDocument(this.eDoc));
        assertFalse(this.wExtractor.isDocument(this.wDoc));
    }

}