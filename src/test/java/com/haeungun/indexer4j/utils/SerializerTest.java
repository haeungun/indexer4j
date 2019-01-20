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

package com.haeungun.indexer4j.utils;

import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SerializerTest {

    private final String filePath = "./test.ser";

    @Test
    public void serializer_test() throws Exception {
        Serializer<String> serializer = new Serializer<>();

        String testStr = "Hello Grace";

        serializer.serializing(this.filePath, testStr);
        String des = serializer.deserializing(this.filePath);

        assertEquals(testStr, des);
    }

    @After
    public void removeFile() {
        File file = new File(this.filePath);

        if (file.exists()) {
            file.delete();
        }
    }

}