package com.systextil.relatorio.dataBaseData;

import java.util.ArrayList;
import java.util.Map;

class SQLGenerator {

	static String generateFinalQuery(String table, ArrayList<String> columns, String conditions, String orderBy,  ArrayList<String> joins) {
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
        
        return query;
    }
    
	static QueryWithTotalizers generateTotalizersQuery(Map<String, Totalizer> totalizers) {
    	String query = "SELECT ";
    	ArrayList<Totalizer> listOfTotalizers = new ArrayList<>();
    	ArrayList<String> tables = new ArrayList<>();
    	boolean firstTotalizer = true;
    	
    	for (Map.Entry<String, Totalizer> totalizer: totalizers.entrySet()) {
    		String tableFromColumn = getTableFromColumn(totalizer.getKey());
    		
    		if (!tables.contains(tableFromColumn)) {
    			tables.add(tableFromColumn);
    		}
    		
    		if (!firstTotalizer) {
    			query = query.concat(", ");
    		}
    		query = query.concat(totalizer.getValue() + "(" + totalizer.getKey() + ")");
    		listOfTotalizers.add(totalizer.getValue());
    		firstTotalizer = false;
    	}
    	query = query.concat(" FROM " + listOfColumnsToQuery(tables));
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
    
    private static String getTableFromColumn(String columnName) {
    	int dotIndex = columnName.indexOf(".");
    	if (dotIndex == -1) {
    		return columnName;
    	} else {
    		return columnName.substring(0, dotIndex);
    	}
    	
    }
}
