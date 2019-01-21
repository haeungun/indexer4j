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

public class SearchResult implements Comparable<SearchResult> {

    private final Object docId;
    private final double score;

    public SearchResult(Object docId, double score) {
        this.docId = docId;
        this.score = score;
    }

    public Object getDocId() {
        return this.docId;
    }


    public double getScore() {
        return this.score;
    }

    @Override
    public int compareTo(SearchResult s) {
        if (this.score < s.getScore()) return -1;
        if (this.score > s.getScore()) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "DocumentScore{docId=" + this.docId
                + ", score=" + this.score + "}";
    }

}
