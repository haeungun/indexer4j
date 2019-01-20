[![Build Status](https://travis-ci.com/haeungun/indexer4j.svg?branch=master)](https://travis-ci.com/haeungun/indexer4j)
[![codecov](https://codecov.io/gh/haeungun/indexer4j/branch/master/graph/badge.svg)](https://codecov.io/gh/haeungun/indexer4j)

# indexer4j
 Simple full text indexing and searching library for Java

## Install
### Gradle
``` gradle
repositories {
    maven {
        url  "https://dl.bintray.com/haeungun/indexer4j"
    }
    // or
    jcenter()
}

dependencies {
   compile 'com.haeungun.indexer4j:indexer4j:<:version>'
}
```

## Features
- Support TF-IDF, BM25 score
- Easy indexing with field annotation

## TODO
- Support ngram, wordgram
- Parrallel build and search
- Support JDK 11 CI on travis CI (Jacoco does not support JDK11 yet)
- Improve saving and loading features

## Examples
```java
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

List<ExampleDocument> documents = Arrays.asList(
            new ExampleDocument("doc1", "First Document", "Lorem Lorem Lorem Lorem Lorem"),
            new ExampleDocument("doc2", "Third Document", "Lorem is hello java python"),
            new ExampleDocument("doc3", "Second Document", "Lorem ipsum dolor"),
            new ExampleDocument("doc4", "Forth Document", "Lorem"));

Indexer<ExampleDocument> index = new Indexer<>();
for (ExampleDocument document : this.documents) {
     index.add(document);
}

index.build();

List<SearchResult> result = index.search("First");
// output => [{docId="doc1", score="..."}]

List<SearchResult> result2 = index.search("Lorem ipsum");
// output = [{docId="doc3", score="..."}, {docId="doc1", score="..."}, ...] 
```
