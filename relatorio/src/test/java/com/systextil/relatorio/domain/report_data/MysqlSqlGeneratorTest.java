package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MysqlSqlGeneratorTest {

	@BeforeAll
	static void setUp() {
		mockStatic(SqlGenerator.class);
	}
	
	@AfterAll
	static void clean() {
		Mockito.clearAllCaches();
	}
	
	@Test
	@DisplayName("generateFinalQueryAnalysis")
	void cenario1() {
		when(SqlGenerator.generateFinalQuery(anyString(), anyList(), anyList(), anyString(), anyList())).thenReturn("SELECT * FROM TABLE");
		
		String finalQueryAnalysis = MysqlSqlGenerator.generateFinalQueryAnalysis("", List.of(), List.of(), "", List.of());
		String expectedFinalQueryAnalysis = "EXPLAIN ANALYZE SELECT * FROM TABLE";
		
		assertEquals(expectedFinalQueryAnalysis, finalQueryAnalysis);
	}
	
	@Test
	@DisplayName("generateTotalizersQueryAnalysis")
	void cenario2() {
		when(SqlGenerator.generateTotalizersQuery(anyMap(), anyString(), anyList(), anyList())).thenReturn("SELECT * FROM TABLE");
		
		String totalizersQueryAnalysis = MysqlSqlGenerator.generateTotalizersQueryAnalysis(Map.of(), "", List.of(), List.of());
		String expectedTotalizersQueryAnalysis = "EXPLAIN ANALYZE SELECT * FROM TABLE";
		
		assertEquals(expectedTotalizersQueryAnalysis, totalizersQueryAnalysis);
	}
}