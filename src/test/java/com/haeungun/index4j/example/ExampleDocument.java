package com.haeungun.index4j.example;

import com.haeungun.index4j.annotation.*;

@Document
public class ExampleDocument {

    @DocumentId
    private String id;

    @DocumentField
    private String title;

    @DocumentField
    private String contents;

    public ExampleDocument(String id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }
}
