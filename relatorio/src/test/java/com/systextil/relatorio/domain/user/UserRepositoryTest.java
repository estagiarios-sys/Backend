package com.systextil.relatorio.domain.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

@SpringBootTest
class UserRepositoryTest {
	
	@Autowired
	private UserRepository repository;
	
	@MockBean
	private OracleConnection connection;
	
	private Connection mockConnection;
	private PreparedStatement mockPreparedStatement;
	private ResultSet mockResultSet;

	@BeforeEach
	void setUp() throws SQLException {		
		mockConnection = mock(Connection.class);
		when(connection.getIdConnection()).thenReturn(mockConnection);
		
		mockPreparedStatement = mock(PreparedStatement.class);
		when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
		
		mockResultSet = mock(ResultSet.class);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
	}
	
	@Test
	@DisplayName("exists: true")
	void cenario1() throws SQLException {
		boolean expectedExists = true;
		when(mockResultSet.next()).thenReturn(expectedExists);
		
		boolean exists = repository.exists("", 0);
		
		assertEquals(expectedExists, exists);
	}
	
	@Test
	@DisplayName("exists: false")
	void cenario2() throws SQLException {
		boolean expectedExists = false;
		when(mockResultSet.next()).thenReturn(expectedExists);
		
		boolean exists = repository.exists("", 0);
		
		assertEquals(expectedExists, exists);
	}
}