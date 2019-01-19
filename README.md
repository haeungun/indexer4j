# index4j
 Simple full text indexing and searching library for Java

## Install
``` mvn
TBA
```

## Features
- Support TF-IDF, BM25 score
- Easy indexing with field annotation

## TODO
- Support ngram, wordgram
- Parrallel build and search

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

private List<ExampleDocument> documents = Arrays.asList(
            new ExampleDocument("doc1", "First Document", "Lorem Lorem Lorem Lorem Lorem"),
            new ExampleDocument("doc2", "Third Document", "Lorem is hello java python"),
            new ExampleDocument("doc3", "Second Document", "Lorem ipsum dolor"),
            new ExampleDocument("doc4", "Forth Document", "Lorem"));

Indexer<ExampleDocument> index = new Indexer<>(Relevance.BM25);
for (ExampleDocument document : this.documents) {
     index.add(document);
}

index.build();

List<SearchResult> result = index.search(query);
```
