package com.systextil.relatorio.domain.report_data;

import java.util.List;
import java.util.Map;

import com.systextil.relatorio.domain.Totalizer;

class MysqlSqlGenerator {
	
	private MysqlSqlGenerator() {
		throw new IllegalStateException("Classe utilitária");
	}

	static String generateFinalQueryAnalysis(String table, List<String> columns, List<String> conditions, String orderBy,  List<String> joins) {
		String finalQuery = SqlGenerator.generateFinalQuery(table, columns, conditions, orderBy, joins);
		
		return "EXPLAIN ANALYZE " + finalQuery;
	}
	
	static String generateTotalizersQueryAnalysis(Map<String, Totalizer> totalizers, String table, List<String> conditions, List<String> joins) {
		String totalizersQuery = SqlGenerator.generateTotalizersQuery(totalizers, table, conditions, joins);

		return "EXPLAIN ANALYZE " + totalizersQuery;
	}
}