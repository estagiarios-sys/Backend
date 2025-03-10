package com.systextil.relatorio.domain.relationship;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.systextil.relatorio.infra.exception_handler.IllegalDataBaseTypeException;

@SpringBootTest
class RelationshipServiceTest {

	@Nested
	@TestPropertySource(properties = "database.type=sqlserver")
	class nest1 {

		@Autowired
		private RelationshipService service;
		
		@MockitoBean
		private RelationshipOracleRepository oracleRepository;
		
		@MockitoBean
		private RelationshipMysqlRepository mysqlRepository;
		
		@MockitoBean
		private MultiJoinReducer multiJoinReducer;
		
		@MockitoBean
		private RelationshipStorageAccessor storageAccessor;
		
		@Test
		@DisplayName("setRelationshipsIntoJson")
		void cenario1() {
			assertThrows(IllegalDataBaseTypeException.class, () -> service.setRelationshipsIntoJson());
		}
	}
	
	@Nested
	@TestPropertySource(properties = "database.type=oracle")
	class nest2 {
		
		@Autowired
		private RelationshipService service;
		
		@MockitoBean
		private RelationshipOracleRepository oracleRepository;
		
		@MockitoBean
		private RelationshipMysqlRepository mysqlRepository;
		
		@MockitoBean
		private MultiJoinReducer multiJoinReducer;
		
		@MockitoBean
		private RelationshipStorageAccessor storageAccessor;
		
		@Test
		@DisplayName("setRelationshipsIntoJson")
		void cenario1() throws SQLException, IOException {
			service.setRelationshipsIntoJson();
			
			verify(oracleRepository).getRelationshipsFromDataBase();
			verify(mysqlRepository, never()).getRelationshipsFromDataBase();
			verify(storageAccessor).setRelationshipsIntoJson(any());
			verify(storageAccessor).setRelationshipsWithJoinsIntoJson(any());
		}
	}

	@Nested
	@TestPropertySource(properties = "database.type=mysql")
	class nest3 {
		
		@Autowired
		private RelationshipService service;
		
		@MockitoBean
		private RelationshipOracleRepository oracleRepository;
		
		@MockitoBean
		private RelationshipMysqlRepository mysqlRepository;
		
		@MockitoBean
		private MultiJoinReducer multiJoinReducer;
		
		@MockitoBean
		private RelationshipStorageAccessor storageAccessor;
		
		@Test
		@DisplayName("setRelationshipsIntoJson")
		void cenario1() throws SQLException, IOException {
			service.setRelationshipsIntoJson();
			
			verify(oracleRepository, never()).getRelationshipsFromDataBase();
			verify(mysqlRepository).getRelationshipsFromDataBase();
			verify(storageAccessor).setRelationshipsIntoJson(any());
			verify(storageAccessor).setRelationshipsWithJoinsIntoJson(any());
		}
	}
}