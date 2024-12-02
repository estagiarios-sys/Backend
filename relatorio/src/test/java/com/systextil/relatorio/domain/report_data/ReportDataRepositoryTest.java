package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
class ReportDataRepositoryTest {
	
	@Autowired
	private H2Connection connection;
	
	@Autowired
	private ReportDataRepository repository;
	
	private Statement statement;

	@BeforeAll
	void setUpAll() throws SQLException {
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
	
	@AfterAll
	void tearDownAll() throws SQLException {
		statement.execute("DROP TABLE CLIENTE");
		statement.execute("DROP TABLE COMPRA");
		connection.disconnect();
	}
	
	@Test
	@DisplayName("findDataByFinalQuery")
	void cenario1() throws SecurityException, SQLException {
        String sql = "SELECT CLIENTE.NOME AS \"NOME DO CLIENTE\", CLIENTE.IDADE, COMPRA.VALOR FROM CLIENTE INNER JOIN COMPRA ON CLIENTE.ID = COMPRA.CLIENTE_ID";
        
        ReportData reportData = repository.findDataByFinalQuery(connection.getIdConnection(), sql);
        
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
	void cenario2() throws SecurityException, SQLException {
		String sql = "SELECT COUNT(CLIENTE.ID), SUM(COMPRA.VALOR) FROM CLIENTE INNER JOIN COMPRA ON CLIENTE.ID = COMPRA.CLIENTE_ID";
		
		System.out.println("Conexao" + connection.getIdConnection());

		List<String> totalizersResults = repository.findDataByTotalizersQuery(connection.getIdConnection(), sql);
		
		List<String> expectedTotalizersResults = List.of("3", "600");
		
		assertEquals(expectedTotalizersResults, totalizersResults);
	}
}