package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MysqlRepositoryTest {

	@Test
	@DisplayName("getActualTimeFromQuery: Deveria retornar os dados corretamente")
	void cenario1() throws SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		Connection mockConnection = mock(Connection.class);
		
		PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		
		String explainAnalyzeText = "bla bla bla actual time=10.5..20.5 bla bla bla\n"
				+ "bla bla bla";
		
		ResultSet mockResultSet = mock(ResultSet.class);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getString(1)).thenReturn(explainAnalyzeText);
		
		MysqlRepository repository = new MysqlRepository();
		
		Method getActualTimeFromQuery = repository.getClass().getDeclaredMethod("getActualTimeFromQuery", Connection.class, String.class);
		getActualTimeFromQuery.setAccessible(true);
		
		int actualTime = (int) getActualTimeFromQuery.invoke(repository, mockConnection, "query");
		
		assertEquals(15, actualTime);
	}
}