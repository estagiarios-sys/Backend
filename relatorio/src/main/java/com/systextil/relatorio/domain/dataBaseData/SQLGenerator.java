package com.systextil.relatorio.domain.dataBaseData;

import java.util.ArrayList;
import java.util.Map;

class SQLGenerator {

	static String generateFinalQuery(String table, ArrayList<String> columns, String conditions, String orderBy,  ArrayList<String> joins) {
        String query = "SELECT ";
        query = query.concat(String.join(", ", columns));
        query = query.concat(" FROM ");
        query = query.concat(table);
        
        if (!joins.isEmpty()) {
            query = query.concat(" ");
            query = query.concat(String.join(" ", joins));
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
    
	static QueryWithTotalizers generateTotalizersQuery(Map<String, Totalizer> totalizers, String table, String conditions, ArrayList<String> joins) {
    	ArrayList<Totalizer> listOfTotalizers = new ArrayList<>();
    	boolean firstTotalizer = true;
    	String query = "SELECT ";
    	
    	for (Map.Entry<String, Totalizer> totalizer: totalizers.entrySet()) {
    		listOfTotalizers.add(totalizer.getValue());
    		
    		if (!firstTotalizer) {
    			query = query.concat(", ");
    		}
    		query = query.concat(totalizer.getValue() + "(" + totalizer.getKey() + ")");
    		firstTotalizer = false;
    	}
    	query = query.concat(" FROM " + table);
    	
    	if (!joins.isEmpty()) {
            query = query.concat(" ");
            query = query.concat(String.join(" ", joins));
        }
    	
    	if (!conditions.isBlank()) {
            query = query.concat(" WHERE ");
            query = query.concat(conditions);
        }
    	QueryWithTotalizers queryWithTotalizers = new QueryWithTotalizers(query, listOfTotalizers);
    	
    	return queryWithTotalizers;
    }
}