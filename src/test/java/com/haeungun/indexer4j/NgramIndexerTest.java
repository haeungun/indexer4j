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

import com.haeungun.indexer4j.core.tokenizer.NgramTokenizer;
import com.haeungun.indexer4j.core.tokenizer.Tokenizer;
import com.haeungun.indexer4j.example.ExampleDocument;
import com.haeungun.indexer4j.exceptions.UndefinedDocumentIdException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NgramIndexerTest {

    private List<ExampleDocument> documents = Arrays.asList(
            new ExampleDocument("doc1", "First", "Lorem ipsum dolor sit amet"),
            new ExampleDocument("doc2", "Second", "arcu convallis adipiscing"),
            new ExampleDocument("doc3", "Third", "In ante donec egestas"),
            new ExampleDocument("doc4", "Forth", "Tempor vel fringilla id")
    );

    @Test
    public void NgramSearch_test() throws UndefinedDocumentIdException {
        Tokenizer tokenizer = new NgramTokenizer(3);
        Indexer<ExampleDocument> index = new Indexer<>(tokenizer);
        for (ExampleDocument document : this.documents) {
            index.add(document);
        }

        index.build();

        String query = "First";
        List<SearchResult> result = index.search(query);

        assertEquals(1, result.size());
        assertEquals("doc1", result.get(0).getDocId());

        String query2 = "Lorem";
        List<SearchResult> result2 = index.search(query2);

        assertEquals(1, result2.size());
        assertEquals("doc1", result2.get(0).getDocId());

        String query3 = "convallis";
        List<SearchResult> result3 = index.search(query3);

        assertEquals(1, result3.size());
        assertEquals("doc2", result3.get(0).getDocId());


        String query4 = "fringilla";
        List<SearchResult> result4 = index.search(query4, 1);

        assertEquals("doc4", result4.get(0).getDocId());
    }

}