package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReportDataRepositoryTest {

	private Connection mockConnection;
	private PreparedStatement mockPreparedStatement;
	private ResultSet mockResultSet;
	private ReportDataRepository repository;
	
	@BeforeEach
	void setUp() throws SQLException {
		mockConnection = mock(Connection.class);
		
		mockPreparedStatement = mock(PreparedStatement.class);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		
		mockResultSet = mock(ResultSet.class);
		
		repository = new ReportDataRepository();
	}
	
	@Test
	@DisplayName("findDataByFinalQuery: Deveria retornar os dados corretamente")
	void cenario1() throws SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        ResultSetMetaData mockMetaData = mock(ResultSetMetaData.class);
        when(mockResultSet.getMetaData()).thenReturn(mockMetaData);
        when(mockMetaData.getColumnCount()).thenReturn(3);

        when(mockMetaData.getColumnLabel(1)).thenReturn("id_cliente");
        when(mockMetaData.getColumnLabel(2)).thenReturn("name");
        when(mockMetaData.getColumnLabel(3)).thenReturn("value");

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString(1)).thenReturn("1", "2");
        when(mockResultSet.getString(2)).thenReturn("John Doe", "Jane Doe");
        when(mockResultSet.getString(3)).thenReturn("100", "200");

        Method findDataByFinalQuery = repository.getClass().getDeclaredMethod("findDataByFinalQuery", Connection.class, String.class);
        findDataByFinalQuery.setAccessible(true);

        ReportData reportData = (ReportData) findDataByFinalQuery.invoke(repository, mockConnection, "SELECT table1.id, table1.name, table1.value FROM table1");

        Map<String, String> expectedColumnsNameAndNickName = new LinkedHashMap<>();
        expectedColumnsNameAndNickName.put("table1.id", "id_cliente");
        expectedColumnsNameAndNickName.put("table1.name", null);
        expectedColumnsNameAndNickName.put("table1.value", null);

        ArrayList<Object[]> expectedFoundObjects = new ArrayList<>();
        expectedFoundObjects.add(new Object[]{"1", "John Doe", "100"});
        expectedFoundObjects.add(new Object[]{"2", "Jane Doe", "200"});

        assertEquals(expectedColumnsNameAndNickName, reportData.columnsNameAndNickName());
        assertArrayEquals(expectedFoundObjects.toArray(), reportData.foundObjects().toArray());
	}
	
	@Test
	@DisplayName("findDataByTotalizersQuery: Deveria retornar os dados corretamente")
	void cenario2() throws SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		
		ResultSetMetaData mockResultSetMetaData = mock(ResultSetMetaData.class);
		when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
		
		when(mockResultSetMetaData.getColumnCount()).thenReturn(3);
		
		when(mockResultSet.getInt(anyInt())).thenReturn(140, 665, 875);
		
		Method findDataByTotalizersQuery = repository.getClass().getDeclaredMethod("findDataByTotalizersQuery", Connection.class, String.class);
		findDataByTotalizersQuery.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		List<String> totalizersResults = (List<String>) findDataByTotalizersQuery.invoke(repository, mockConnection, "query");
		
		List<String> expectedTotalizersResults = List.of("140", "665", "875");
		
		assertEquals(expectedTotalizersResults, totalizersResults);
	}
}