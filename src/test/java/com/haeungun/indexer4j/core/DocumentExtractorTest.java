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

import static org.junit.Assert.*;

import com.haeungun.indexer4j.DocumentMeta;
import com.haeungun.indexer4j.core.tokenizer.RegexTokenizer;
import com.haeungun.indexer4j.core.tokenizer.Tokenizer;
import com.haeungun.indexer4j.example.ExampleDocument;
import com.haeungun.indexer4j.example.WrongDocument;
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