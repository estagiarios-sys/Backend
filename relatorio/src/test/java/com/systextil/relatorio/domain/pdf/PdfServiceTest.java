package com.systextil.relatorio.domain.pdf;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
	
	private MockedStatic<StorageAccessor> mockedStorageAccessor;
	
	@BeforeAll
	void setUp() {
		mockStatic(LocalDateTime.class);
		mockedStorageAccessor = mockStatic(StorageAccessor.class);
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
		mockedStorageAccessor.verify(() -> StorageAccessor.deleteFile(anyString()), never());
	}
	
	@Test
	@DisplayName("createNoDataPdf: tem 10 pdf no banco")
	void cenario2() throws IOException {
		when(mockRepository.save(any())).thenReturn(mockPdf);
		when(mockRepository.count()).thenReturn(10L);
		when(mockRepository.findPathById(anyLong())).thenReturn("");
		
		service.createNoDataPdf("Título");
		
		verify(mockRepository).deleteById(anyLong());
		mockedStorageAccessor.verify(() -> StorageAccessor.deleteFile(anyString()));
	}
	
	@Test
	@DisplayName("generatePdf: microserviço retorna 200")
	void cenario3() throws IOException {
		PdfSaving mockPdfSaving = mock(PdfSaving.class);
		ResponseEntity mockResponseEntity = mock(ResponseEntity.ok(Byte.valueOf("123")));
		when(mockRestTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), )).thenReturn(mockResponseEntity);
		when(mockRepository.getReferenceById(anyLong())).thenReturn(mockPdf);

		service.generatePdf(mockPdfSaving);
		
		mockedStorageAccessor.verify(() -> StorageAccessor.savePdf(any(byte[].class), anyString()));
		verify(mockPdf).update(any(LocalDateTime.class), anyString());
	}
	
	@Test
	@DisplayName("generatePdf: microserviço retorna 400")
	void cenario4() {
		
	}
}