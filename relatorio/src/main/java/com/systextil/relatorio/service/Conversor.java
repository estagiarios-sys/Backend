package com.systextil.relatorio.service;

import java.util.List;

public class Conversor {

    public static String listToQuery(List<String> colunas) {
        String query = "";
        for (String coluna : colunas) {
            if (!coluna.equals(colunas.getFirst())) {
                query = query.concat(", ");
            }
            query = query.concat(coluna);
        }
        return query;
    }

}
