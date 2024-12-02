package com.systextil.relatorio.domain.report_data;

import java.util.List;
import java.util.Map;

import com.systextil.relatorio.domain.Totalizer;
import static com.systextil.relatorio.domain.report_data.SqlGenerator.generateFinalQuery;
import static com.systextil.relatorio.domain.report_data.SqlGenerator.generateTotalizersQuery;

class OracleSqlGenerator {
	
	private OracleSqlGenerator() {
		throw new IllegalStateException("Classe utilitária");
	}

	static String[] generateFinalQueryAnalysis(String table, List<String> joinColumnsNameAndNickName, List<String> conditions, String orderBy, List<String> joinsByTablesPairs) {
		String sql = generateFinalQuery(table, joinColumnsNameAndNickName, conditions, orderBy, joinsByTablesPairs);
		
		return new String[] {"EXPLAIN PLAN FOR " + sql, "SELECT SUM(time) FROM plan_table"};
	}

	static String[] generateTotalizersQueryAnalysis(Map<String, Totalizer> totalizers, String table, List<String> conditions, List<String> joinsByTablesPairs) {
		String sql = generateTotalizersQuery(totalizers, table, conditions, joinsByTablesPairs);
		
		return new String[] {"EXPLAIN PLAN FOR " + sql, "SELECT SUM(time) FROM plan_table"};
	}

}
