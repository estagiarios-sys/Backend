package com.systextil.relatorio.domain.table;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.systextil.relatorio.infra.exception_handler.IllegalDataBaseTypeException;

@SpringBootTest
class TableServiceTest {

	@Nested
	@TestPropertySource(properties = "database.type=sqlserver")
	@DisplayName("database.type=sqlserver")
	class nest1 {
		
		@Autowired
		private TableService service;
		
		@MockitoBean
		private TableOracleRepository oracleRepository;
		
		@MockitoBean
		private TableMysqlRepository mysqlRepository;
		
		@MockitoBean
		private TableStorageAccessor storageAccessor;
		
		@Test
		@DisplayName("getColumnsFromTables")
		void cenario1() {
			AllTables allTables = mock(AllTables.class);
			assertThrows(IllegalDataBaseTypeException.class, () -> service.getColumnsFromTables(allTables));
		}
		
		@Test
		@DisplayName("setTablesIntoJson")
		void cenario2() throws IOException {
			assertThrows(IllegalDataBaseTypeException.class, () -> service.setTablesIntoJson());
			
			verify(storageAccessor, never()).setTablesIntoJson(any());
		}
	}
	
	@Nested
	@TestPropertySource(properties = "database.type=oracle")
	@DisplayName("database.type=oracle")
	class nest2 {
		
		@Autowired
		private TableService service;
		
		@MockitoBean
		private TableOracleRepository oracleRepository;
		
		@MockitoBean
		private TableMysqlRepository mysqlRepository;
		
		@MockitoBean
		private TableStorageAccessor storageAccessor;
		
		@Test
		@DisplayName("getColumnsFromTables")
		void cenario1() throws SQLException {
			Map<String, String> columnsFromMainTable = Map.of("COLUNA1", "VARCHAR");
			Map<String, String> columnsFromTablesPair = Map.of("COLUNA1", "INT");
			
			when(oracleRepository.getColumnsFromTable(anyString())).thenReturn(columnsFromTablesPair);
			when(oracleRepository.getColumnsFromTable("TABELA1")).thenReturn(columnsFromMainTable);
			
			AllTables allTables = new AllTables("TABELA1", List.of("TABELA1 e TABELA2", "TABELA2 e TABELA3"));
			Map<String, Map<String, String>> tablesAndColumns = service.getColumnsFromTables(allTables);
			
			Map<String, Map<String, String>> expectedTablesAndColumns = new LinkedHashMap<>();
			expectedTablesAndColumns.put("TABELA1", columnsFromMainTable);
			expectedTablesAndColumns.put("TABELA2", columnsFromTablesPair);
			expectedTablesAndColumns.put("TABELA3", columnsFromTablesPair);
			
			assertEquals(expectedTablesAndColumns, tablesAndColumns);
		}
		
		@Test
		@DisplayName("setTablesIntoJson")
		void cenario2() throws IOException, SQLException {
			service.setTablesIntoJson();
		
			verify(oracleRepository).getTables();
			verify(storageAccessor).setTablesIntoJson(any());
		}
	}
	
	@Nested
	@TestPropertySource(properties = "database.type=mysql")
	@DisplayName("database.type=mysql")
	class nest3 {
		
		@Autowired
		private TableService service;
		
		@MockitoBean
		private TableOracleRepository oracleRepository;
		
		@MockitoBean
		private TableMysqlRepository mysqlRepository;
		
		@MockitoBean
		private TableStorageAccessor storageAccessor;
		
		@Test
		@DisplayName("getColumnsFromTables")
		void cenario1() throws SQLException {
			Map<String, String> columnsFromMainTable = Map.of("COLUNA1", "VARCHAR");
			Map<String, String> columnsFromTablesPair = Map.of("COLUNA1", "INT");
			
			when(mysqlRepository.getColumnsFromTable(anyString())).thenReturn(columnsFromTablesPair);
			when(mysqlRepository.getColumnsFromTable("TABELA1")).thenReturn(columnsFromMainTable);
			
			AllTables allTables = new AllTables("TABELA1", List.of("TABELA1 e TABELA2", "TABELA2 e TABELA3"));
			Map<String, Map<String, String>> tablesAndColumns = service.getColumnsFromTables(allTables);
			
			Map<String, Map<String, String>> expectedTablesAndColumns = new LinkedHashMap<>();
			expectedTablesAndColumns.put("TABELA1", columnsFromMainTable);
			expectedTablesAndColumns.put("TABELA2", columnsFromTablesPair);
			expectedTablesAndColumns.put("TABELA3", columnsFromTablesPair);
			
			assertEquals(expectedTablesAndColumns, tablesAndColumns);
		}
		
		@Test
		@DisplayName("setTablesIntoJson")
		void cenario2() throws IOException, SQLException {
			service.setTablesIntoJson();
		
			verify(mysqlRepository).getTables();
			verify(storageAccessor).setTablesIntoJson(any());
		}
	}
}