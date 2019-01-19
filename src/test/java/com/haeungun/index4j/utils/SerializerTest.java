package com.haeungun.index4j.utils;

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