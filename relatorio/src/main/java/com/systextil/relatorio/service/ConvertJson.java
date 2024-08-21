package com.systextil.relatorio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.entity.Tabela;
import java.io.IOException;

public class ConvertJson {

    public Tabela convertJson(String json) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Tabela.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}