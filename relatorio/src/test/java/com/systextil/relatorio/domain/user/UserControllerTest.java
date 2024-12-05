package com.systextil.relatorio.domain.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WithMockUser
class UserControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private JacksonTester<LoginRequest> loginRequestJson;
	
	@MockitoBean
	private UserRepository repository;
	
	@MockitoBean
	private UserService service;
	
	@Test
	@DisplayName("login: Todos parâmetros")
	void cenario1() throws Exception {
		String token = "123";
		ResponseEntity<String> serviceResponse = new ResponseEntity<String>(token, HttpStatus.OK);
		
		when(service.login(any())).thenReturn(serviceResponse);
		
		String response = mvc
				.perform(
					post("http://localhost:8082/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(loginRequestJson.write(
						new LoginRequest(1, "login", "senha")
					).getJson())
				)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		
		assertEquals(token, response);
	}
	
	@Test
	@DisplayName("login: Parâmetro codigoEmpresa negativo")
	void cenario2() throws Exception {
		mvc
		.perform(
			post("http://localhost:8082/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(loginRequestJson.write(
				new LoginRequest(-1, "login", "senha")
			).getJson())
		)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("login: Parâmetro login vazio")
	void cenario3() throws Exception {
		mvc
		.perform(
			post("http://localhost:8082/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(loginRequestJson.write(
				new LoginRequest(1, "", "senha")
			).getJson())
		)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("login: Parâmetro senha vazio")
	void cenario4() throws Exception {
		mvc
		.perform(
			post("http://localhost:8082/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(loginRequestJson.write(
				new LoginRequest(1, "login", "")
			).getJson())
		)
		.andExpect(status().isBadRequest());
	}
}