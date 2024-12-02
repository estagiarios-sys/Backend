package com.systextil.relatorio.domain.relationship;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.infra.data_base_connection.H2Connection;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class RelationshipRepositoryTest {

	@Autowired
	private RelationshipRepository repository;
	
	@Autowired
	private H2Connection connection;
	
	private Statement statement;
	
	@BeforeAll
	void setUpAll() throws SQLException {
		connection.connect();
		statement = connection.getIdConnection().createStatement();
		statement.execute("CREATE TABLE INFORMATION_SCHEMA (TABLE_NAME VARCHAR(255), COLUMN_NAME VARCHAR(255), REFERENCED_TABLE_NAME VARCHAR(255), REFERENCED_COLUMN_NAME VARCHAR(255))");
		statement.execute("INSERT INTO INFORMATION_SCHEMA VALUES ('TABELA1', 'COLUNA1', 'TABELA2', 'COLUNA2')");
	}
	
	@AfterAll
	void tearDownAll() throws SQLException {
		statement.execute("DROP TABLE INFORMATION_SCHEMA");
		statement.close();
		connection.disconnect();
	}
	
	@Test
	@DisplayName("getRelationshipsFromDataBase")
	void cenario1() throws SQLException {
		String sql = "SELECT * FROM INFORMATION_SCHEMA";
		
		List<RelationshipData> relationships = repository.getRelationshipsFromDataBase(connection.getIdConnection(), sql);
		
		List<RelationshipData> expectedRelationships = new ArrayList<>();
		expectedRelationships.add(new RelationshipData("TABELA1 e TABELA2", "INNER JOIN TABELA2 ON TABELA1.COLUNA1 = TABELA2.COLUNA2"));
		expectedRelationships.add(new RelationshipData("TABELA2 e TABELA1", "INNER JOIN TABELA1 ON TABELA2.COLUNA2 = TABELA1.COLUNA1"));
		
		assertEquals(expectedRelationships, relationships);
	}
}