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

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentScoreTest {

    @Test
    public void compareTo_test() {
        SearchResult score = new SearchResult("doc1", 2.0);
        SearchResult score2 = new SearchResult("doc2", 1.0);

        assertEquals(1, score.compareTo(score2));
    }

    @Test
    public void toString_test() {
        SearchResult score = new SearchResult("doc1", 2.0);

        assertEquals("DocumentScore{docId=doc1, score=2.0}", score.toString());
    }
}