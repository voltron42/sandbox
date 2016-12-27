package com.voltron42.junitPractice.parse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by daniel.johnson on 12/27/2016.
 */
public class Parser<T> {

    public static class Factory {
        public <T> Parser<T> build(Class<T> type) {
            return new Parser<T>(new ObjectMapper(), type);
        }
    }

    private final ObjectMapper objectMapper;
    private final Class<T> type;

    public Parser(ObjectMapper objectMapper, Class<T> type) {
        this.objectMapper = objectMapper;
        this.type = type;
    }

    public T parse(InputStream src) throws IOException {
        return objectMapper.readValue(src, type);
    }
}
