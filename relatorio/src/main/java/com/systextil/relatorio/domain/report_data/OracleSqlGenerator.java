package com.systextil.relatorio.domain.report_data;

import static com.systextil.relatorio.domain.report_data.SqlGenerator.*;

import java.util.List;
import java.util.Map;

import com.systextil.relatorio.domain.Totalizer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

class OracleSqlGenerator {

	static String[] generateFinalQueryAnalysis(@NotBlank String table, List<String> joinColumnsNameAndNickName,
			@NotNull List<String> conditions, @NotNull String orderBy, List<String> joinsByTablesPairs) {
		
		String sql = generateFinalQuery(table, joinColumnsNameAndNickName, conditions, orderBy, joinsByTablesPairs);
		
		return new String[] {"EXPLAIN PLAN FOR " + sql, "SELECT SUM(time) FROM plan_table"};
	}

	static String[] generateTotalizersQueryAnalysis(@NotNull Map<String, Totalizer> totalizers,
			@NotBlank String table, @NotNull List<String> conditions, List<String> joinsByTablesPairs) {
		
		String sql = generateTotalizersQuery(totalizers, table, conditions, joinsByTablesPairs);
		
		return new String[] {"EXPLAIN PLAN FOR " + sql, "SELECT SUM(time) FROM plan_table"};
	}

}
