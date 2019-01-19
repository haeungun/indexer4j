package com.haeungun.index4j;

public class SearchResult implements Comparable<SearchResult> {

    private final String docId;
    private final double score;

    public SearchResult(String docId, double score) {
        this.docId = docId;
        this.score = score;
    }

    public String getDocId() {
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
