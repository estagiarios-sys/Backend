package com.systextil.relatorio.service;

import java.util.ArrayList;

public class SQLGenerator {

    public static String finalQuery(String table, ArrayList<String> columns, String conditions, String orderBy,  ArrayList<String> joins) {
        String query = "SELECT ";
        query = query.concat(listOfColumnsToQuery(columns));
        query = query.concat(" FROM ");
        query = query.concat(table);
        if (!joins.isEmpty()) {
            query = query.concat(" ");
            query = query.concat(listOfJoinsToQuery(joins));
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

    private static String listOfColumnsToQuery(ArrayList<String> colunas) {
        String query = "";
        for (String coluna : colunas) {
            if (!coluna.equals(colunas.getFirst())) {
                query = query.concat(", ");
            }
            query = query.concat(coluna);
        }
        return query;
    }

    private static String listOfJoinsToQuery(ArrayList<String> joins) {
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
