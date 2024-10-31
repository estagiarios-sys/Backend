package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class OracleRepositoryTest {

	@Test
	@DisplayName("getActualTimeFromQuery")
	void cenario1() throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Connection mockConnection = mock(Connection.class);
		
		PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		
		ResultSet mockResulsSet = mock(ResultSet.class);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResulsSet);
		when(mockResulsSet.getInt(1)).thenReturn(10);
		
		OracleRepository repository = new OracleRepository();
		
		Method getActualTimeFromQuery = repository.getClass().getDeclaredMethod("getActualTimeFromQuery", Connection.class, String[].class);
		getActualTimeFromQuery.setAccessible(true);
		
		int actualTime = (int) getActualTimeFromQuery.invoke(repository, mockConnection, new String[] {"query1", "query2"});
		
		assertEquals(10, actualTime);
	}
}