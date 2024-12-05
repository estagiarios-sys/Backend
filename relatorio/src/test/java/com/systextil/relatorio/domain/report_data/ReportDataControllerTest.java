package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
class ReportDataControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private JacksonTester<QueryData> queryDataJson;
	
	@Autowired
	private JacksonTester<Object[]> objectArrayJson;
	
	@Autowired
	private JacksonTester<Integer> intJson;
	
	@MockitoBean
	private ReportDataService service;
	
	@Test
	@DisplayName("getQueryReturn: Todos parâmetros")
	void cenario1() throws Exception {
		Object[] queryReturn = {"query", "query", List.of(), List.of(), Map.of()};
		when(service.getQueryReturn(any())).thenReturn(queryReturn);

		String response = mvc
				.perform(post("/report-data")
					.contentType(MediaType.APPLICATION_JSON)
					.content(queryDataJson.write(
							new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), "", List.of(), Map.of())
					).getJson())
				)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String expectedQueryReturn = objectArrayJson.write(queryReturn).getJson();
		
		assertEquals(expectedQueryReturn, response);
	}

	@Test
	@DisplayName("getQueryReturn: Parâmetro table vazio")
	void cenario2() throws Exception {
		mvc
		.perform(post("/report-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), "", List.of(), Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryReturn(any());
	}
	
	@Test
	@DisplayName("getQueryReturn: Parâmetro columns vazio")
	void cenario3() throws Exception {
		mvc
		.perform(post("/report-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(), List.of(), "", List.of(), Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryReturn(any());
	}
	
	@Test
	@DisplayName("getQueryReturn: Parâmetro conditions nulo")
	void cenario4() throws Exception {
		mvc
		.perform(post("/report-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), null, "", List.of(), Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryReturn(any());
	}
	
	@Test
	@DisplayName("getQueryReturn: Parâmetro orderBy nulo")
	void cenario5() throws Exception {
		mvc
		.perform(post("/report-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), null, List.of(), Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryReturn(any());
	}
	
	@Test
	@DisplayName("getQueryReturn: Parâmetro tablesPairs nulo")
	void cenario6() throws Exception {
		mvc
		.perform(post("/report-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), "", null, Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryReturn(any());
	}
	
	@Test
	@DisplayName("getQueryReturn: Parâmetro totalizers nulo")
	void cenario7() throws Exception {
		mvc
		.perform(post("/report-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), "", List.of(), null)
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryReturn(any());
	}
	
	@Test
	@DisplayName("getQueryAnalysis: Todos parâmetros")
	void cenario8() throws Exception {
		int queryAnalysis = 5;
		when(service.getQueryAnalysis(any())).thenReturn(queryAnalysis);

		String response = mvc
				.perform(post("/report-data/analyze")
						.contentType(MediaType.APPLICATION_JSON)
						.content(queryDataJson.write(
								new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), "", List.of(), Map.of())
						).getJson())
				)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String expectedQueryAnalysis = intJson.write(queryAnalysis).getJson();
		
		assertEquals(expectedQueryAnalysis, response);
	}

	@Test
	@DisplayName("getQueryAnalysis: Parâmetro table vazio")
	void cenario9() throws Exception {
		mvc
		.perform(post("/report-data/analyze")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), "", List.of(), Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryAnalysis(any());
	}
	
	@Test
	@DisplayName("getQueryAnalysis: Parâmetro columns vazio")
	void cenario10() throws Exception {
		mvc
		.perform(post("/report-data/analyze")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(), List.of(), "", List.of(), Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryAnalysis(any());
	}
	
	@Test
	@DisplayName("getQueryAnalysis: Parâmetro conditions nulo")
	void cenario11() throws Exception {
		mvc
		.perform(post("/report-data/analyze")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), null, "", List.of(), Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryAnalysis(any());
	}
	
	@Test
	@DisplayName("getQueryAnalysis: Parâmetro orderBy nulo")
	void cenario12() throws Exception {
		mvc
		.perform(post("/report-data/analyze")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), null, List.of(), Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryAnalysis(any());
	}
	
	@Test
	@DisplayName("getQueryAnalysis: Parâmetro tablesPairs nulo")
	void cenario13() throws Exception {
		mvc
		.perform(post("/report-data/analyze")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), "", null, Map.of())
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryAnalysis(any());
	}
	
	@Test
	@DisplayName("getQueryAnalysis: Parâmetro totalizers nulo")
	void cenario14() throws Exception {
		mvc
		.perform(post("/report-data/analyze")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryDataJson.write(
						new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), "", List.of(), null)
				).getJson())
		)
		.andExpect(status().isBadRequest());

		verify(service, never()).getQueryAnalysis(any());
	}
}