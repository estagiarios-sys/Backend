package com.systextil.relatorio.domain.user;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

@TestInstance(Lifecycle.PER_CLASS)
class UserRepositoryTest {

	private H2Connection h2Connection;
	private UserRepository repository;
	private Statement statement;

	@BeforeAll
	void setUpAll() throws SQLException {
		h2Connection = new H2Connection();
		h2Connection.connect();
		
		statement = h2Connection.getIdConnection().createStatement();
		statement.execute("CREATE TABLE FATU_500 (CODIGO_EMPRESA INT, NOME_EMPRESA VARCHAR(255))");
		statement.execute("INSERT INTO FATU_500 (CODIGO_EMPRESA, NOME_EMPRESA) VALUES (500, 'EMPRESA')");
		
		statement.execute("CREATE TABLE HDOC_030 (EMPRESA INT, USUARIO VARCHAR(255), SENHA VARCHAR(255))");
		statement.execute("INSERT INTO HDOC_030 (EMPRESA, USUARIO, SENHA) VALUES (1, 'AAA', 'AAA')");
		statement.execute("INSERT INTO HDOC_030 (EMPRESA, USUARIO, SENHA) VALUES (1, 'BBB', 'BBB')");
		statement.execute("INSERT INTO HDOC_030 (EMPRESA, USUARIO, SENHA) VALUES (555, 'CCC', 'CCC')");		
	}
	
	@BeforeEach
	void setUp() {
		repository = new UserRepository(h2Connection);
	}
	
	@AfterAll
	void tearDownAll() throws SQLException {
		statement.execute("DROP TABLE FATU_500");
		statement.execute("DROP TABLE HDOC_030");
		h2Connection.disconnect();
	}
	
	@Test
	@DisplayName("exists: true")
	void cenario1() throws SQLException {
		boolean exists = repository.exists("AAA", 1);
		
		assertEquals(true, exists);
	}
	
	@Test
	@DisplayName("exists: false")
	void cenario2() throws SQLException {
		boolean exists = repository.exists("AAA", 555);
		
		assertEquals(false, exists);
	}
	
	private class H2Connection extends OracleConnection {
		private H2Connection() {
			fillAttributes("sa", "", "jdbc:h2:mem:testdb");
		}
	}
}