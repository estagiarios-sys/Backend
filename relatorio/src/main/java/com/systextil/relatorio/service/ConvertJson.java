package com.systextil.relatorio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConvertJson {

    private String tabela = "";
    private List<String> colunas;
    private String where = "";
    private String orderBy = "";
    private String join = "";

    public ConvertJson() {
        try {
            Resource resource = new ClassPathResource("teste.json");
            File file = resource.getFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonStructure jsonStructure = objectMapper.readValue(file, JsonStructure.class);

            tabela = jsonStructure.tabela;
            colunas = jsonStructure.colunas;
            where = jsonStructure.where;
            orderBy = jsonStructure.orderBy;
            join = jsonStructure.join;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class JsonStructure {
        public String tabela;
        public List<String> colunas;
        public String where;
        public String orderBy;
        public String join;
    }

    public String getJoin() {
        return join;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getWhere() {
        return where;
    }

    public List<String> getColunas() {
        return colunas;
    }

    public String getTabela() {
        return tabela;
    }
}