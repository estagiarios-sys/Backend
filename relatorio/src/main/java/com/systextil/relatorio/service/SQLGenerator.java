package com.systextil.relatorio.service;

import java.util.ArrayList;
import java.util.List;

public class SQLGenerator {

    public static String finalQuery(String table, ArrayList<String> columns, String conditions, String orderBy,  ArrayList<String> joins) {
        String query = "SELECT ";
        query = query.concat(listToQuery(columns));
        query = query.concat(" FROM ");
        query = query.concat(table);
        if (!joins.isEmpty()) {
            query = query.concat(" ");
            query = query.concat(listToQueryJoin(joins));
        }
        if (!conditions.isBlank()) {
            query = query.concat(" WHERE ");
            query = query.concat(conditions);
        }
        if (!orderBy.isEmpty()) {
            query = query.concat(" ORDER BY ");
            query = query.concat(orderBy);
        }
        System.out.println(query);
        return query;
    }

    private static String listToQuery(List<String> colunas) {
        String query = "";
        for (String coluna : colunas) {
            if (!coluna.equals(colunas.getFirst())) {
                query = query.concat(", ");
            }
            query = query.concat(coluna);
        }
        return query;
    }

    private static String listToQueryJoin(ArrayList<String> joins) {
        String query = "";
        for (String join : joins) {
            if (!join.equals(joins.getFirst())) {
                query = query.concat(" ");
            }
            query = query.concat(join);
        }
        return query;
    }
}
