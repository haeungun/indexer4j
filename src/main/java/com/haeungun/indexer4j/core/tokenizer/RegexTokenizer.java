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

package com.haeungun.indexer4j.core.tokenizer;

import java.util.Arrays;
import java.util.List;

public class RegexTokenizer implements Tokenizer {

    private final String regex;

    public RegexTokenizer(String regex) {
        this.regex = regex;
    }

    @Override
    public List<String> tokenizing(String str) {
        return Arrays.asList(str.split(regex));
    }

}
