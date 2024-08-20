package com.systextil.relatorio.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Conversor {

    public static String finalQuery(String nome, List<String> colunas, ArrayList<String> condicoes, String orderBy, String join) {
        String query = "SELECT ";
        query = query.concat(listToQuery(colunas));
        query = query.concat(" FROM ");
        query = query.concat(nome);
        if (!join.equals("")) {
            query = query.concat(" INNER JOIN ");
            query = query.concat(join);
        }
        if (!condicoes.equals("")) {
            query = query.concat(" WHERE ");
            for (String condicao : condicoes) {
                query = query.concat(condicao);
            }
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
