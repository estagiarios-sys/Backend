package com.systextil.relatorio.domain.report_data;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class ReportDataOracleRepositoryTest {

	@MockitoBean
	private ReportDataRepository repository;

	private final H2Connection connection;
	private ReportDataOracleRepository oracleRepository;
	private Statement statement;
	
	ReportDataOracleRepositoryTest() {
		this.connection = new H2Connection();
	}
	
	private class H2Connection extends OracleConnection {
		private H2Connection() {
			fillAttributes("sa", "", "jdbc:h2:mem:testdb");
		}
	}
	
	@BeforeAll
	void setUpAll() throws SQLException {
		connection.connect();
		
		statement = connection.getIdConnection().createStatement();
		statement.execute("CREATE TABLE TEST (TEST INT)");
		statement.execute("CREATE TABLE PLAN_TABLE (TIME INT)");
		for (int i = 0; i < 3; i++) {
			statement.execute("INSERT INTO PLAN_TABLE VALUES (1)");
		}
	}
	
	@BeforeEach
	void setUp() {
		oracleRepository = new ReportDataOracleRepository(connection, repository);
	}
	
	@AfterAll
	void tearDownAll() throws SQLException {
		statement.execute("DROP TABLE PLAN_TABLE");
		
		statement.close();
		connection.disconnect();
	}
	
	@Test
	@DisplayName("getActualTimeFromQueries: Todos parâmetros não vazios")
	void cenario1() throws SQLException {
		String[] finalQueryAnalysis = {"EXPLAIN PLAN FOR SELECT 1 FROM TEST", "SELECT SUM(TIME) FROM PLAN_TABLE"};
		String[] totalizersQueryAnalysis = {"EXPLAIN PLAN FOR SELECT SUM(1) FROM TEST", "SELECT SUM(TIME) FROM PLAN_TABLE"};
		int actualTime = oracleRepository.getActualTimeFromQueries(finalQueryAnalysis, totalizersQueryAnalysis);
		
		assertEquals(6, actualTime);
	}
	
	@Test
	@DisplayName("getActualTimeFromQueries: Parâmetro totalizersQueryAnalysis vazio")
	void cenario2() throws SQLException {
		String[] finalQueryAnalysis = {"EXPLAIN PLAN FOR SELECT 1 FROM TEST", "SELECT SUM(TIME) FROM PLAN_TABLE"};
		int actualTime = oracleRepository.getActualTimeFromQueries(finalQueryAnalysis, null);
		
		assertEquals(3, actualTime);
	}
}