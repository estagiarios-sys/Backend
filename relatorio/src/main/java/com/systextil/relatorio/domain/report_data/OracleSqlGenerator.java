package com.systextil.relatorio.domain.report_data;

import java.util.List;
import java.util.Map;

import com.systextil.relatorio.domain.Totalizer;

class OracleSqlGenerator {
	
	private OracleSqlGenerator() {
		throw new IllegalStateException("Classe utilirária");
	}
	
	static String[] generateFinalQueryAnalysis(String table, List<String> columns, List<String> conditions, String orderBy,  List<String> joins) {
		String finalQuery = SqlGenerator.generateFinalQuery(table, columns, conditions, orderBy, joins);

		return new String[] {"EXPLAIN PLAN FOR " + finalQuery, "SELECT SUM(time) FROM plan_table"};
	}
	
	static String[] generateTotalizersQueryAnalysis(Map<String, Totalizer> totalizers, String table, List<String> conditions, List<String> joins) {
		String totalizersQuery = SqlGenerator.generateTotalizersQuery(totalizers, table, conditions, joins);

		return new String[] {"EXPLAIN PLAN FOR " + totalizersQuery, "SELECT SUM(time) FROM plan_table"};
	}
}