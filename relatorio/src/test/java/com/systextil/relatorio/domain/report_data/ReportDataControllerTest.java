package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.mock.web.MockHttpServletResponse;
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
	private JacksonTester<QueryData> requestJson;
	
	@Autowired
	private JacksonTester<Object[]> responseJson;
	
	@MockitoBean
	private ReportDataService service;
	
	@Test
	@DisplayName("getQueryReturn: response = bad request")
	void cenario1() throws Exception {
		MockHttpServletResponse response = mvc
				.perform(post("/report-data"))
				.andReturn()
				.getResponse();

		assertEquals(400, response.getStatus());
	}

	@Test
	@DisplayName("getQueryReturn: response = ok com dados")
	void cenario2() throws Exception {
		Object[] queryReturn = {"query", "query", List.of(), List.of(), Map.of()};

		when(service.getQueryReturn(any())).thenReturn(queryReturn);

		MockHttpServletResponse response = mvc
				.perform(post("/report-data")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson.write(
								new QueryData("TABELA", List.of(new QueryDataColumn("NOME", "APELIDO")), List.of(), "", List.of(), Map.of())
						).getJson())
				)
				.andReturn().getResponse();

		String expectedQueryReturn = responseJson.write(queryReturn).getJson();
		
		assertEquals(200, response.getStatus());
		assertEquals(expectedQueryReturn, response.getContentAsString());
	}
}