package com.systextil.relatorio.service;

import java.util.List;

public class Conversor {

    public static String finalQuery(List<String> colunas, String tabela, String where, String orderBy, String join) {
        String query = "SELECT ";
        query = query.concat(listToQuery(colunas));
        query = query.concat(" FROM ");
        query = query.concat(tabela);
        if (!join.equals("")) {
            query = query.concat(" INNER JOIN ");
            query = query.concat(join);
        }
        if (!where.equals("")) {
            query = query.concat(" WHERE ");
            query = query.concat(where);
        }
        if (!orderBy.equals("")) {
            query = query.concat(" ORDER BY ");
            query = query.concat(orderBy);
        }
        System.out.println(query);
        return query;
    }

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
