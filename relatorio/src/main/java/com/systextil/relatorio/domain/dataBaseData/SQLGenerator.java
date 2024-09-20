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
    
	static String generateTotalizersQuery(Map<String, Totalizer> totalizers, String table, String conditions, ArrayList<String> joins) {
    	boolean firstTotalizer = true;
    	String query = "SELECT ";
    	
    	for (Map.Entry<String, Totalizer> totalizer: totalizers.entrySet()) {
    		
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
    	
    	return query;
    }
	
	static String generateFinalQueryAnalysisFromMySQLDataBase (String table, ArrayList<String> columns, String conditions, String orderBy,  ArrayList<String> joins) {
		String finalQuery = generateFinalQuery(table, columns, conditions, orderBy, joins);
		
		return "EXPLAIN ANALYZE " + finalQuery;
	}
	
	static String generateTotalizersQueryAnalysisFromMySQLDataBase(Map<String, Totalizer> totalizers, String table, String conditions, ArrayList<String> joins) {
		String totalizersQuery = generateTotalizersQuery(totalizers, table, conditions, joins);
		
		return "EXPLAIN ANALYZE " + totalizersQuery;
	}
	
	static String[] generateFinalQueryAnalysisFromOracleDataBase (String table, ArrayList<String> columns, String conditions, String orderBy,  ArrayList<String> joins) {
		String finalQuery = generateFinalQuery(table, columns, conditions, orderBy, joins);
		String[] finalQueryAnalysis = {"EXPLAIN PLAN FOR " + finalQuery, "SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY)"};
		
		return finalQueryAnalysis;
	}
	
	static String[] generateTotalizersQueryAnalysisFromOracleDataBase(Map<String, Totalizer> totalizers, String table, String conditions, ArrayList<String> joins) {
		String totalizersQuery = generateTotalizersQuery(totalizers, table, conditions, joins);
		String[] totalizersQueryAnalysis = {"EXPLAIN PLAN FOR " + totalizersQuery, "SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY)"};
		
		return totalizersQueryAnalysis;
	}
}