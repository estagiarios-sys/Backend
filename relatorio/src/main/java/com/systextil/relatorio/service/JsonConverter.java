package com.systextil.relatorio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.entity.SavedQuery;
import com.systextil.relatorio.record.QueryData;
import java.io.IOException;

public class JsonConverter {

    public QueryData jsonTable(String json) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, QueryData.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SavedQuery jsonSavedQuery(String json) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, SavedQuery.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}