package com.systextil.relatorio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.entity.Tabela;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;

public class ConvertJson {

    public Tabela convertJson() {
        try {
            Resource resource = new ClassPathResource("teste.json");
            File file = resource.getFile();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(file, Tabela.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}