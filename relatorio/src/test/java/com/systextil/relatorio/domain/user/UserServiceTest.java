package com.systextil.relatorio.domain.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static br.com.intersys.systextil.global.Criptografia.desembaralha;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.systextil.relatorio.infra.jwt.JwtService;

import br.com.intersys.systextil.global.Criptografia;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class UserServiceTest {

	@Autowired
	private UserService service;
	
	@MockitoBean
	private UserRepository repository;
	
	@MockitoBean
	private JwtService jwtService;
	
	@BeforeAll
	void setUpAll() {
		mockStatic(Criptografia.class);
	}
	
	@AfterAll
	void tearDownAll() {
		clearAllCaches();
	}
	
	@Test
	@DisplayName("login: desembaralha não lança exception e senha está correta")
	void cenario1() throws SQLException {
		when(repository.exists(anyString(), anyInt())).thenReturn(true);
		when(desembaralha(any())).thenReturn("123");
		when(jwtService.generateToken(anyString())).thenReturn("token");
		
		LoginRequest loginRequest = new LoginRequest(1, "login", "123");
		ResponseEntity<String> response = service.login(loginRequest);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals("token", response.getBody());
	}
	
	@Test
	@DisplayName("login: desembaralha não lança exception e senha está incorreta")
	void cenario2() throws SQLException {
		when(repository.exists(anyString(), anyInt())).thenReturn(true);
		when(desembaralha(any())).thenReturn("321");
		
		LoginRequest loginRequest = new LoginRequest(1, "login", "123");
		ResponseEntity<String> response = service.login(loginRequest);
		
		assertEquals(401, response.getStatusCode().value());
		assertEquals("", response.getBody());
	}
	
	@Test
	@DisplayName("login: desembaralha lança exception e senha está correta")
	void cenario3() throws SQLException {
		when(repository.exists(anyString(), anyInt())).thenReturn(true);
		when(repository.getSenha(anyString(), anyInt())).thenReturn("123");
		when(desembaralha(any())).thenThrow(NumberFormatException.class);
		when(jwtService.generateToken(anyString())).thenReturn("token");
		
		LoginRequest loginRequest = new LoginRequest(1, "login", "123");
		ResponseEntity<String> response = service.login(loginRequest);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals("token", response.getBody());
	}

	@Test
	@DisplayName("login: desembaralha lança exception e senha está incorreta")
	void cenario4() throws SQLException {
		when(repository.exists(anyString(), anyInt())).thenReturn(true);
		when(repository.getSenha(anyString(), anyInt())).thenReturn("321");

		LoginRequest loginRequest = new LoginRequest(1, "login", "123");
		ResponseEntity<String> response = service.login(loginRequest);
		
		assertEquals(401, response.getStatusCode().value());
		assertEquals("", response.getBody());
	}
	
	@Test
	@DisplayName("login: usuário não existe")
	void cenario5() throws SQLException {
		when(repository.exists(anyString(), anyInt())).thenReturn(false);
		
		LoginRequest loginRequest = new LoginRequest(1, "login", "123");
		ResponseEntity<String> response = service.login(loginRequest);
		
		assertEquals(404, response.getStatusCode().value());
	}
}