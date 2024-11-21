package com.systextil.relatorio.domain.user;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

@TestInstance(Lifecycle.PER_CLASS)
class UserRepositoryTest {

	private H2Connection h2Connection;
	private UserRepository repository;
	private Statement statement;
	
	private class H2Connection extends OracleConnection {
		private H2Connection() {
			fillAttributes("sa", "", "jdbc:h2:mem:testdb");
		}
	}

	@BeforeAll
	void setUpAll() throws SQLException {
		h2Connection = new H2Connection();
		h2Connection.connect();
		
		statement = h2Connection.getIdConnection().createStatement();
		statement.execute("CREATE TABLE FATU_500 (CODIGO_EMPRESA INT, NOME_EMPRESA VARCHAR(255))");
		statement.execute("INSERT INTO FATU_500 (CODIGO_EMPRESA, NOME_EMPRESA) VALUES (1, 'EMPRESA 1')");
		statement.execute("INSERT INTO FATU_500 (CODIGO_EMPRESA, NOME_EMPRESA) VALUES (555, 'EMPRESA 555')");
		
		statement.execute("CREATE TABLE HDOC_030 (EMPRESA INT, USUARIO VARCHAR(255), SENHA VARCHAR(255))");
		statement.execute("INSERT INTO HDOC_030 (EMPRESA, USUARIO, SENHA) VALUES (1, 'AAA', 'SENHA AAA')");
		statement.execute("INSERT INTO HDOC_030 (EMPRESA, USUARIO, SENHA) VALUES (1, 'BBB', 'SENHA BBB')");
		statement.execute("INSERT INTO HDOC_030 (EMPRESA, USUARIO, SENHA) VALUES (555, 'CCC', 'SENHA CCC')");		
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
	
	@Test
	@DisplayName("getSenha: assertEquals")
	void cenario3() throws SQLException {
		String senha = repository.getSenha("AAA", 1);

		assertEquals("SENHA AAA", senha);
	}
	
	@Test
	@DisplayName("getSenha: assertNotEquals")
	void cenario4() throws SQLException {
		String senha = repository.getSenha("BBB", 1);
		
		assertNotEquals("SENHA AAA", senha);
	}
	
	@Test
	@DisplayName("getUser: assertEquals")
	void cenario5() throws SQLException {
		UserDetails expectedUserDetails = new User("AAA", "SENHA AAA", Collections.emptyList());
		
		UserDetails userDetails = repository.getUser("AAA");
		
		assertEquals(expectedUserDetails, userDetails);
	}
	
	@Test
	@DisplayName("getUser: assertNotEquals")
	void cenario6() throws SQLException {
		UserDetails expectedUserDetails = new User("AAA", "SENHA AAA", Collections.emptyList());
		
		UserDetails userDetails = repository.getUser("BBB");
		
		assertNotEquals(expectedUserDetails, userDetails);
	}
	
	@Test
	@DisplayName("getCompanies")
	void cenario7() throws SQLException {
		Company firstExpectedCompany = new Company(1, "EMPRESA 1");
		Company secondExpectedCompany = new Company(555, "EMPRESA 555");
		List<Company> expectedCompanies = new ArrayList<>();
		expectedCompanies.add(firstExpectedCompany);
		expectedCompanies.add(secondExpectedCompany);
		
		List<Company> companies = repository.getCompanies();
		
		assertIterableEquals(expectedCompanies, companies);
	}
}