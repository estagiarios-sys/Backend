package com.systextil.relatorio.service;

import java.util.List;

public class Conversor {

    public static String listToQuery(List<String> colunas) {
        String query = "";
        query = query.concat("SELECT ");
        for (String coluna : colunas) {
            if (!coluna.equals(colunas.getFirst())) {
                query = query.concat(", ");
            }
            query = query.concat(coluna);
        }
        query = query.concat("FROM Cliente");
        return query;
    }

}
