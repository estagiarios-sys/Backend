package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.systextil.relatorio.infra.data_base_connection.H2Connection;

@TestInstance(Lifecycle.PER_CLASS)
class ReportDataRepositoryTest {

	private H2Connection connection;
	private Statement statement;
	private ReportDataRepository repository;
	
	@BeforeAll
	void setUpAll() throws SQLException {
		connection = new H2Connection();
		connection.connect();
		
		statement = connection.getIdConnection().createStatement();
		statement.execute("CREATE TABLE CLIENTE (ID INT, NOME VARCHAR(255), IDADE INT)");
		statement.execute("INSERT INTO CLIENTE VALUES (1, 'AAA', 10)");
		statement.execute("INSERT INTO CLIENTE VALUES (2, 'BBB', 20)");
		statement.execute("INSERT INTO CLIENTE VALUES (3, 'CCC', 30)");
		
		statement.execute("CREATE TABLE COMPRA (ID INT, VALOR FLOAT, CLIENTE_ID INT)");
		statement.execute("INSERT INTO COMPRA VALUES (1, 100.0, 3)");
		statement.execute("INSERT INTO COMPRA VALUES (2, 200.0, 2)");
		statement.execute("INSERT INTO COMPRA VALUES (3, 300.0, 1)");
	}
	
	@BeforeEach
	void setUp() {
		repository = new ReportDataRepository();
	}
	
	@AfterAll
	void tearDownAll() throws SQLException {
		statement.execute("DROP TABLE CLIENTE");
		statement.execute("DROP TABLE COMPRA");
		connection.disconnect();
	}
	
	@Test
	@DisplayName("findDataByFinalQuery")
	void cenario1() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
        Method findDataByFinalQuery = ReportDataRepository.class.getDeclaredMethod("findDataByFinalQuery", Connection.class, String.class);
        findDataByFinalQuery.setAccessible(true);
        
        String sql = "SELECT CLIENTE.NOME AS \"NOME DO CLIENTE\", CLIENTE.IDADE, COMPRA.VALOR FROM CLIENTE INNER JOIN COMPRA ON CLIENTE.ID = COMPRA.CLIENTE_ID";
        ReportData reportData = (ReportData) findDataByFinalQuery.invoke(repository, connection.getIdConnection(), sql);
        
        Map<String, String> expectedColumnsNameAndNickName = new LinkedHashMap<>();
        expectedColumnsNameAndNickName.put("CLIENTE.NOME", "NOME DO CLIENTE");
        expectedColumnsNameAndNickName.put("CLIENTE.IDADE", null);
        expectedColumnsNameAndNickName.put("COMPRA.VALOR", null);
        
        List<Object[]> expectedListObjects = new ArrayList<>();
        expectedListObjects.add(new Object[] {"AAA", "10", "300.0"});
        expectedListObjects.add(new Object[] {"BBB", "20", "200.0"});
        expectedListObjects.add(new Object[] {"CCC", "30", "100.0"});
        
        assertEquals(expectedColumnsNameAndNickName, reportData.columnsNameAndNickName());
        assertArrayEquals(expectedListObjects.toArray(), reportData.foundObjects().toArray());
	}
	
	@Test
	@DisplayName("findDataByTotalizersQuery")
	void cenario2() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		Method findDataByTotalizersQuery = repository.getClass().getDeclaredMethod("findDataByTotalizersQuery", Connection.class, String.class);
		findDataByTotalizersQuery.setAccessible(true);
		
		String sql = "SELECT COUNT(CLIENTE.ID), SUM(COMPRA.VALOR) FROM CLIENTE INNER JOIN COMPRA ON CLIENTE.ID = COMPRA.CLIENTE_ID";
		@SuppressWarnings("unchecked")
		List<String> totalizersResults = (List<String>) findDataByTotalizersQuery.invoke(repository, connection.getIdConnection(), sql);
		
		List<String> expectedTotalizersResults = List.of("3", "600");
		
		assertEquals(expectedTotalizersResults, totalizersResults);
	}
}