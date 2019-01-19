package com.haeungun.index4j.core.relevance;

import java.util.List;

public interface RelevanceRanker {

    double rank(String term, List<String> doc, List<List<String>> docs);

}
