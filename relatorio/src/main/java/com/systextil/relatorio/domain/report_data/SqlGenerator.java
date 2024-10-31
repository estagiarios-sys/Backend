package com.systextil.relatorio.domain.report_data;

import java.util.List;
import java.util.Map;

import com.systextil.relatorio.domain.Totalizer;

class SqlGenerator {
	
	private SqlGenerator() {
		throw new IllegalStateException("Utility class");
	}

	static String generateFinalQuery(String table, List<String> columns, List<String> conditions, String orderBy,  List<String> joins) {
        String query = "SELECT ";
        query = query.concat(String.join(", ", columns));
        query = query.concat(" FROM ");
        query = query.concat(table);
        
        if (!joins.isEmpty()) {
            query = query.concat(" ");
            query = query.concat(String.join(" ", joins));
        }
        
        if (!conditions.isEmpty()) {
            query = query.concat(" WHERE ");
            query = query.concat(String.join(" AND ", conditions));
        }
        
        if (!orderBy.isBlank()) {
            query = query.concat(" ORDER BY ");
            query = query.concat(orderBy);
        }
        
        return query;
    }
    
	static String generateTotalizersQuery(Map<String, Totalizer> totalizers, String table, List<String> conditions, List<String> joins) {
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
    	if (!conditions.isEmpty()) {
            query = query.concat(" WHERE ");
            query = query.concat(String.join(" AND ", conditions));
        }
    	return query;
    }
	
	static String generateFinalQueryAnalysisFromMysql(String table, List<String> columns, List<String> conditions, String orderBy,  List<String> joins) {
		String finalQuery = generateFinalQuery(table, columns, conditions, orderBy, joins);
		
		return "EXPLAIN ANALYZE " + finalQuery;
	}
	
	static String generateTotalizersQueryAnalysisFromMysql(Map<String, Totalizer> totalizers, String table, List<String> conditions, List<String> joins) {
		String totalizersQuery = generateTotalizersQuery(totalizers, table, conditions, joins);
		
		return "EXPLAIN ANALYZE " + totalizersQuery;
	}
	
	static String[] generateFinalQueryAnalysisFromOracle(String table, List<String> columns, List<String> conditions, String orderBy,  List<String> joins) {
		String finalQuery = generateFinalQuery(table, columns, conditions, orderBy, joins);
		
		return new String[] {"EXPLAIN PLAN FOR " + finalQuery, "SELECT SUM(time) FROM plan_table"};
	}
	
	static String[] generateTotalizersQueryAnalysisFromOracle(Map<String, Totalizer> totalizers, String table, List<String> conditions, List<String> joins) {
		String totalizersQuery = generateTotalizersQuery(totalizers, table, conditions, joins);
		
		return new String[] {"EXPLAIN PLAN FOR " + totalizersQuery, "SELECT SUM(time) FROM plan_table"};
	}
}