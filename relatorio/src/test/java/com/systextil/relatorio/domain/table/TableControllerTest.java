package com.systextil.relatorio.domain.table;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WithMockUser
class TableControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private JacksonTester<AllTables> atalitonTester;
	
	@Autowired
	private JacksonTester<Map<String, Map<String, String>>> gustavoTester;
	
	@MockitoBean
	private TableService service;
	
	@Test
	@DisplayName("getColumnsFromTables: Todos parâmetros")
	void cenario1() throws Exception {
		Map<String, Map<String, String>> columnsFromTables = Map.of("TABELA1", Map.of("COLUNA1", "VARCHAR"));
		when(service.getColumnsFromTables(any())).thenReturn(columnsFromTables);
		
		String response = mvc
				.perform(post("http://localhost:8082/tables/columns")
					.contentType(MediaType.APPLICATION_JSON)
					.content(atalitonTester.write(
						new AllTables("TABELA1", List.of())
					).getJson())
				)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		
		String expectedResponse = gustavoTester.write(columnsFromTables).getJson();
		
		assertEquals(expectedResponse, response);
	}
	
	@Test
	@DisplayName("getColumnsFromTables: Parâmetro mainTable vazio")
	void cenario2() throws Exception {
		mvc.
		perform(post("http://localhost:8082/tables/columns")
			.contentType(MediaType.APPLICATION_JSON)
			.content(atalitonTester.write(
				new AllTables("", List.of())
			).getJson())
		)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("getColumnsFromTables: Parâmetro tablesPairs nulo")
	void cenario3() throws Exception {
		mvc.
		perform(post("http://localhost:8082/tables/columns")
			.contentType(MediaType.APPLICATION_JSON)
			.content(atalitonTester.write(
				new AllTables("TABELA1", null)
			).getJson())
		)
		.andExpect(status().isBadRequest());
	}
}