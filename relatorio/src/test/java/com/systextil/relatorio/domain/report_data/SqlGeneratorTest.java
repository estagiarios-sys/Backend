package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.systextil.relatorio.domain.Totalizer;

class SqlGeneratorTest {

	@Test
	@DisplayName("generateFinalQuery: Todos parâmetros não vazios")
	void cenario1() {
		String query = SqlGenerator.generateFinalQuery(
				"TABELA1",
				List.of("COLUNA1", "COLUNA2"),
				List.of("COLUNA1 > 100", "COLUNA2 < 100"),
				"COLUNA1 DESC",
				List.of("JOIN TABELA2 ON COLUNA1 = COLUNA2", "JOIN TABELA3 ON COLUNA1 = COLUNA2"),
				""
				);
		String expectedQuery = "SELECT COLUNA1, COLUNA2 "
								+ "FROM TABELA1 "
								+ "JOIN TABELA2 ON COLUNA1 = COLUNA2 "
								+ "JOIN TABELA3 ON COLUNA1 = COLUNA2 "
								+ "WHERE COLUNA1 > 100 AND COLUNA2 < 100 "
								+ "ORDER BY COLUNA1 DESC";
		
		assertEquals(expectedQuery, query);
	}
	
	@Test
	@DisplayName("generateFinalQuery: Parâmetro conditions vazio")
	void cenario2() {
		String query = SqlGenerator.generateFinalQuery(
				"TABELA1",
				List.of("COLUNA1", "COLUNA2"),
				Collections.emptyList(),
				"COLUNA1 DESC",
				List.of("JOIN TABELA2 ON COLUNA1 = COLUNA2", "JOIN TABELA3 ON COLUNA1 = COLUNA2"),
				""
				);
		String expectedQuery = "SELECT COLUNA1, COLUNA2 "
								+ "FROM TABELA1 "
								+ "JOIN TABELA2 ON COLUNA1 = COLUNA2 "
								+ "JOIN TABELA3 ON COLUNA1 = COLUNA2 "
								+ "ORDER BY COLUNA1 DESC";
		
		assertEquals(expectedQuery, query);
	}

	@Test
	@DisplayName("generateFinalQuery: Parâmetro orderBy vazio")
	void cenario3() {
		String query = SqlGenerator.generateFinalQuery(
				"TABELA1",
				List.of("COLUNA1", "COLUNA2"),
				List.of("COLUNA1 > 100", "COLUNA2 < 100"),
				"",
				List.of("JOIN TABELA2 ON COLUNA1 = COLUNA2", "JOIN TABELA3 ON COLUNA1 = COLUNA2"),
				""
				);
		String expectedQuery = "SELECT COLUNA1, COLUNA2 "
								+ "FROM TABELA1 "
								+ "JOIN TABELA2 ON COLUNA1 = COLUNA2 "
								+ "JOIN TABELA3 ON COLUNA1 = COLUNA2 " 
								+ "WHERE COLUNA1 > 100 AND COLUNA2 < 100";		
		assertEquals(expectedQuery, query);
	}
	
	@Test
	@DisplayName("generateFinalQuery: Parâmetro joins vazio")
	void cenario4() {
		String query = SqlGenerator.generateFinalQuery(
				"TABELA1",
				List.of("COLUNA1", "COLUNA2"),
				List.of("COLUNA1 > 100", "COLUNA2 < 100"),
				"COLUNA1 DESC",
				Collections.emptyList(),
				""
				);
		String expectedQuery = "SELECT COLUNA1, COLUNA2 "
								+ "FROM TABELA1 "
								+ "WHERE COLUNA1 > 100 AND COLUNA2 < 100 "
								+ "ORDER BY COLUNA1 DESC";
		
		assertEquals(expectedQuery, query);
	}
	
	@Test
	@DisplayName("generateTotalizersQuery: Todos parâmetros não vazios")
	void cenario5() {
		Map<String, Totalizer> totalizers = new LinkedHashMap<>();
		totalizers.put("COLUNA1", Totalizer.AVG);
		totalizers.put("COLUNA2", Totalizer.SUM);
		
		String query = SqlGenerator.generateTotalizersQuery(
				totalizers,
				"TABELA1",
				List.of("COLUNA1 > 100", "COLUNA2 < 100"),
				List.of("JOIN TABELA2 ON COLUNA1 = COLUNA2", "JOIN TABELA3 ON COLUNA1 = COLUNA2")
				);
		String expectedQuery = "SELECT AVG(COLUNA1), SUM(COLUNA2) "
								+ "FROM TABELA1 "
								+ "JOIN TABELA2 ON COLUNA1 = COLUNA2 "
								+ "JOIN TABELA3 ON COLUNA1 = COLUNA2 "
								+ "WHERE COLUNA1 > 100 AND COLUNA2 < 100";
		
		assertEquals(expectedQuery, query);
	}
	
	@Test
	@DisplayName("generateTotalizersQuery: Parâmetro conditions vazio")
	void cenario6() {
		Map<String, Totalizer> totalizers = new LinkedHashMap<>();
		totalizers.put("COLUNA1", Totalizer.AVG);
		totalizers.put("COLUNA2", Totalizer.SUM);
		
		String query = SqlGenerator.generateTotalizersQuery(
				totalizers,
				"TABELA1",
				Collections.emptyList(),
				List.of("JOIN TABELA2 ON COLUNA1 = COLUNA2", "JOIN TABELA3 ON COLUNA1 = COLUNA2")
				);
		String expectedQuery = "SELECT AVG(COLUNA1), SUM(COLUNA2) "
								+ "FROM TABELA1 "
								+ "JOIN TABELA2 ON COLUNA1 = COLUNA2 "
								+ "JOIN TABELA3 ON COLUNA1 = COLUNA2";
		
		assertEquals(expectedQuery, query);
	}
	
	@Test
	@DisplayName("generateTotalizersQuery: Parâmetro joins vazio")
	void cenario7() {
		Map<String, Totalizer> totalizers = new LinkedHashMap<>();
		totalizers.put("COLUNA1", Totalizer.AVG);
		totalizers.put("COLUNA2", Totalizer.SUM);
		
		String query = SqlGenerator.generateTotalizersQuery(
				totalizers,
				"TABELA1",
				List.of("COLUNA1 > 100", "COLUNA2 < 100"),
				Collections.emptyList()
				);
		String expectedQuery = "SELECT AVG(COLUNA1), SUM(COLUNA2) "
								+ "FROM TABELA1 "
								+ "WHERE COLUNA1 > 100 AND COLUNA2 < 100";
		
		assertEquals(expectedQuery, query);
	}
}