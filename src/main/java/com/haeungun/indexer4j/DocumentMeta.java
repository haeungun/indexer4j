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

import java.util.List;

public class DocumentMeta {

    private final String docId;
    private final List<String> tokenizedWords;

    public DocumentMeta(String docId, List<String> tokenizedWords) {
        this.docId = docId;
        this.tokenizedWords = tokenizedWords;
    }

    public String getDocId() {
        return this.docId;
    }

    public List<String> getTokenizedWords() {
        return this.tokenizedWords;
    }

    @Override
    public String toString() {
        return "DocumentMeta{docId=" + this.docId
                + ", tokenizedWords=" + this.tokenizedWords.toString() + "}";
    }
}
