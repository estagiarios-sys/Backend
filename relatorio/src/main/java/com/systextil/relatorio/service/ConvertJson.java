package com.systextil.relatorio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.entity.SaveQuery;
import com.systextil.relatorio.entity.TableData;
import java.io.IOException;

public class ConvertJson {

    public TableData jsonTable(String json) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, TableData.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SaveQuery jsonConsultaSalva(String json) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, SaveQuery.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}