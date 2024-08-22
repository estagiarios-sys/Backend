package com.systextil.relatorio.service;

import java.util.ArrayList;
import java.util.List;

public class SQLGenerator {

    public static String finalQuery(String nome, ArrayList<String> colunas, ArrayList<String> condicoes, String orderBy,  ArrayList<String> join) {
        String query = "SELECT ";
        query = query.concat(listToQuery(colunas));
        query = query.concat(" FROM ");
        query = query.concat(nome);
        if (!join.isEmpty()) {
            query = query.concat(" ");
            query = query.concat(listToQueryJoin(join));
        }
        if (!condicoes.isEmpty()) {
            query = query.concat(" WHERE ");
            query = query.concat(listToQueryConditions(condicoes));
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

    private static String listToQueryConditions(ArrayList<String> conditions) {
        String query = "";
        for (String condition : conditions) {
            if (!condition.equals(conditions.getFirst())) {
                query = query.concat(" AND ");
            }
            query = query.concat(condition);
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
