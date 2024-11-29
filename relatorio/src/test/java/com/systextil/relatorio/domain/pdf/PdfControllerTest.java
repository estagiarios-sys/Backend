package com.systextil.relatorio.domain.pdf;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
class PdfControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JacksonTester<PdfSaving> pdfSavingJson;

	@Autowired
	private JacksonTester<MicroserviceRequest> microserviceRequestJson;

	@MockitoBean
	private PdfRepository repository;

	@MockitoBean
	private PdfService service;

	@Test
	@DisplayName("generatePdf: Todos parâmetros")
	void cenario1() throws Exception {
		mvc
		.perform(
				put("http://localhost:8082/pdf/set-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(pdfSavingJson.write(
						new PdfSaving(1L, "<html></html>", "imagem")
						).getJson()))
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("generatePdf: Parâmetro pdfId negativo")
	void cenario2() throws Exception {
		mvc
		.perform(
				put("http://localhost:8082/pdf/set-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(pdfSavingJson.write(
						new PdfSaving(-1L, "<html></html>", "imagem")
						).getJson()))
		.andExpect(status().isBadRequest());
		
		verify(service, never()).setStatusGerandoPdf(any());
	}
	
	@Test
	@DisplayName("generatePdf: Parâmetro fullTableHTML em branco")
	void cenario3() throws Exception {
		mvc
		.perform(
				put("http://localhost:8082/pdf/set-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(pdfSavingJson.write(
						new PdfSaving(1L, "", "imagem")
						).getJson()))
		.andExpect(status().isBadRequest());
		
		verify(service, never()).setStatusGerandoPdf(any());
	}
	
	@Test
	@DisplayName("generatePdf: Parâmetro imgPDF nulo")
	void cenario4() throws Exception {
		mvc
		.perform(
				put("http://localhost:8082/pdf/set-data")
				.contentType(MediaType.APPLICATION_JSON)
				.content(pdfSavingJson.write(
						new PdfSaving(1L, "<html></html>", null)
						).getJson()))
		.andExpect(status().isBadRequest());
		
		verify(service, never()).setStatusGerandoPdf(any());
	}
	
	@Test
	@DisplayName("previewPdf: Todos parâmetros")
	void cenario5() throws Exception {
		byte[] pdfPreview = {1, 2, 3};
		
		when(service.previewPdf(any())).thenReturn(pdfPreview);
		
		String responseType = mvc
				.perform(
						post("http://localhost:8082/pdf/preview")
						.contentType(MediaType.APPLICATION_JSON)
						.content(microserviceRequestJson.write(
								new MicroserviceRequest("<html></html>", "", "")
								).getJson()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentType();
		
		assertEquals("application/pdf", responseType);
	}
	
	@Test
	@DisplayName("previewPdf: Parâmetro fullTableHTML em branco")
	void cenario6() throws Exception {
		mvc
		.perform(
				post("http://localhost:8082/pdf/preview")
				.contentType(MediaType.APPLICATION_JSON)
				.content(microserviceRequestJson.write(
						new MicroserviceRequest("", "", "")
						).getJson()))
		.andExpect(status().isBadRequest());
		
		verify(service, never()).previewPdf(any());
	}
	
	@Test
	@DisplayName("previewPdf: Parâmetro titlePDF nulo")
	void cenario7() throws Exception {
		mvc
		.perform(
				post("http://localhost:8082/pdf/preview")
				.contentType(MediaType.APPLICATION_JSON)
				.content(microserviceRequestJson.write(
						new MicroserviceRequest("<html></html>", null, "")
						).getJson()))
		.andExpect(status().isBadRequest());
		
		verify(service, never()).previewPdf(any());
	}
	
	@Test
	@DisplayName("previewPdf: Parâmetro imgPDF nulo")
	void cenario8() throws Exception {
		mvc
		.perform(
				post("http://localhost:8082/pdf/preview")
				.contentType(MediaType.APPLICATION_JSON)
				.content(microserviceRequestJson.write(
						new MicroserviceRequest("<html></html>", "", null)
						).getJson()))
		.andExpect(status().isBadRequest());
		
		verify(service, never()).previewPdf(any());
	}
}