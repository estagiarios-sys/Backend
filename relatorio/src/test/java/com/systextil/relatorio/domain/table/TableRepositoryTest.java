package com.systextil.relatorio.domain.table;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.systextil.relatorio.infra.data_base_connection.H2Connection;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class TableRepositoryTest {

	@Autowired
	private TableRepository repository;
	
	@Autowired
	private H2Connection connection;
	
	private Statement statement;
	
	@BeforeAll
	void setUpAll() throws SQLException {
		connection.connect();
		statement = connection.getIdConnection().createStatement();
		statement.execute("CREATE SCHEMA SA");
		statement.execute("CREATE TABLE SA.CLIENTE (ID INT, NOME VARCHAR(255))");
		statement.execute("CREATE TABLE SA.VENDA (ID INT, VALOR FLOAT)");
	}
	
	@AfterAll
	void tearDownAll() throws SQLException {
		statement.execute("DROP TABLE SA.CLIENTE");
		statement.execute("DROP TABLE SA.VENDA");
		statement.close();
		connection.disconnect();
	}
	
	@Test
	@DisplayName("getTables")
	void cenario1() throws SQLException {
		List<String> tables = repository.getTables(connection.getIdConnection(), null);
		
		List<String> expectedTables = List.of("CLIENTE", "VENDA");
		
		assertEquals(expectedTables, tables);
	}

	@Test
	@DisplayName("getColumnsFromTable")
	void cenario2() throws SQLException {
		Map<String, String> columns = repository.getColumnsFromTable(connection.getIdConnection(), "CLIENTE");
		
		Map<String, String> expectedColumns = new LinkedHashMap<>();
		expectedColumns.put("ID", "INTEGER");
		expectedColumns.put("NOME", "CHARACTER VARYING");
		
		assertEquals(expectedColumns, columns);
	}
}