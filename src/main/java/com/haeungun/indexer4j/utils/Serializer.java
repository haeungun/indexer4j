package com.haeungun.indexer4j.utils;

import java.io.*;

public class Serializer<T> {

    public void serializing(String filePath, T t) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream out = new ObjectOutputStream(bos)){

            out.writeObject(t);
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public T deserializing(String filePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream in = new ObjectInputStream(bis)){

            return (T) in.readObject();
        } catch (Exception e) {
            throw e;
        }
    }
}