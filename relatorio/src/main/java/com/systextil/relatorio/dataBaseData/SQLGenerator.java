package com.systextil.relatorio.dataBaseData;

import java.util.ArrayList;
import java.util.Map;

public class SQLGenerator {

    public static String generateFinalQuery(String table, ArrayList<String> columns, String conditions, String orderBy,  ArrayList<String> joins) {
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
    
    public static QueryWithTotalizers generateTotalizersQuery(Map<String, Totalizer> totalizers, String table) {
    	String query = "SELECT ";
    	ArrayList<Totalizer> listOfTotalizers = new ArrayList<>();
    	boolean firstTotalizer = true;
    	
    	for (Map.Entry<String, Totalizer> totalizer: totalizers.entrySet()) {
    		
    		if (!firstTotalizer) {
    			query = query.concat(", ");
    		}
    		query = query.concat(totalizer.getValue() + "(" + totalizer.getKey() + ")");
    		listOfTotalizers.add(totalizer.getValue());
    		firstTotalizer = false;
    	}
    	query = query.concat(" FROM " + table);
    	QueryWithTotalizers queryWithTotalizers = new QueryWithTotalizers(query, listOfTotalizers);
    	
    	return queryWithTotalizers;
    	
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
