package com.systextil.relatorio.domain.pdf;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class PdfServiceTest {

	@Autowired
	private PdfService service;
	
	@MockBean
	private PdfRepository mockRepository;
	
	@MockBean
	private Pdf mockPdf;
	
	@MockBean
	private RestTemplate mockRestTemplate;
	
	@MockBean
	private HttpHeaders mockHeaders;
	
	@MockBean
	private HttpEntity<MicroserviceRequest> mockRequest;
	
	@MockBean
	private MicroserviceRequest mockMicroserviceRequest;
	
	private MockedStatic<Files> mockedFile;
	
	@BeforeAll
	void setUp() {
		mockStatic(LocalDateTime.class);
		mockedFile = mockStatic(Files.class);
		mockStatic(Paths.class);
	}
	
	@AfterAll
	void clean() {
		Mockito.clearAllCaches();
	}
	
	@Test
	@DisplayName("createNoDataPdf: tem menos de 10 pdfs no banco")
	void cenario1() throws IOException {
		when(mockRepository.save(any())).thenReturn(mockPdf);
		when(mockRepository.count()).thenReturn(9L);
		
		service.createNoDataPdf("Título");
		
		verify(mockRepository, never()).deleteById(anyLong());
		mockedFile.verify(() -> Files.delete(any()), never());
	}
	
	@Test
	@DisplayName("createNoDataPdf: tem 10 pdf no banco")
	void cenario2() throws IOException {
		when(mockRepository.save(any())).thenReturn(mockPdf);
		when(mockRepository.count()).thenReturn(10L);
		when(mockRepository.findPathById(anyLong())).thenReturn("");
		
		service.createNoDataPdf("Título");
		
		verify(mockRepository).deleteById(anyLong());
		mockedFile.verify(() -> Files.delete(any()));
	}
	
	@Test
	@DisplayName("generatePdf")
	void cenario3() {
	}
	
	
}