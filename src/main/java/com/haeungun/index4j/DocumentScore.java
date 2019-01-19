package com.haeungun.index4j;

public class DocumentScore implements Comparable<DocumentScore> {

    private final String docId;
    private final double score;

    public DocumentScore(String docId, double score) {
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
    public int compareTo(DocumentScore s) {
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
