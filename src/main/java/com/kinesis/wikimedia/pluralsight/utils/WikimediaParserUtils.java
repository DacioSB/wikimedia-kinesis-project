package com.kinesis.wikimedia.pluralsight.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinesis.wikimedia.pluralsight.WikimediaRepresentation;

public class WikimediaParserUtils {
    public static WikimediaRepresentation parseWikimedia(String wikimediaJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(wikimediaJson, WikimediaRepresentation.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
