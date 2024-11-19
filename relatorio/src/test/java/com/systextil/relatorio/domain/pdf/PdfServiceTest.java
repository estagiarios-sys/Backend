package com.systextil.relatorio.domain.pdf;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.systextil.relatorio.infra.exception_handler.UnsupportedHttpStatusException;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class PdfServiceTest {

	@Autowired
	private PdfService service;
	
	@MockBean
	private PdfRepository mockRepository;
	
	@MockBean
	private MicroserviceClient mockMicroserviceClient;
	
	private MockedStatic<StorageAccessor> mockedStorageAccessor;
	private Pdf mockPdf;
	
	@BeforeAll
	void staticMocks() {
		mockedStorageAccessor = mockStatic(StorageAccessor.class);
	}
	
	@BeforeEach
	void mocks() {
		mockPdf = mock(Pdf.class);
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
	@DisplayName("generatePdf: microserviço retorna 2xx")
	void cenario3() throws IOException {
		when(mockRepository.getReferenceById(anyLong())).thenReturn(mockPdf);
		
		@SuppressWarnings("unchecked")
		ResponseEntity<byte[]> mockResponse = mock(ResponseEntity.class);
		when(mockMicroserviceClient.generatePdf(any())).thenReturn(mockResponse);
		
		when(mockResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
		
		PdfSaving mockPdfSaving = mock(PdfSaving.class);
		service.generatePdf(mockPdfSaving);
		
		mockedStorageAccessor.verify(() -> StorageAccessor.savePdf(any(), any()));
		verify(mockPdf).update(any(), any());
	}
	
	@Test
	@DisplayName("generatePdf: microserviço retorna 4xx")
	void cenario4() throws IOException {
		when(mockRepository.getReferenceById(anyLong())).thenReturn(mockPdf);
		
		@SuppressWarnings("unchecked")
		ResponseEntity<byte[]> mockResponse = mock(ResponseEntity.class);
		when(mockMicroserviceClient.generatePdf(any())).thenReturn(mockResponse);
		
		when(mockResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(400));
		
		PdfSaving mockPdfSaving = mock(PdfSaving.class);
		
		try {
			service.generatePdf(mockPdfSaving);
		} catch (HttpClientErrorException exception) {
			verify(mockPdf).update(PdfStatus.ERRO);
			return;
		}
		fail();
	}
	
	@Test
	@DisplayName("generatePdf: microserviço retorna 5xx")
	void cenario5() throws IOException {
		when(mockRepository.getReferenceById(anyLong())).thenReturn(mockPdf);
		
		@SuppressWarnings("unchecked")
		ResponseEntity<byte[]> mockResponse = mock(ResponseEntity.class);
		when(mockMicroserviceClient.generatePdf(any())).thenReturn(mockResponse);
		
		when(mockResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(500));
		
		PdfSaving mockPdfSaving = mock(PdfSaving.class);
		
		try {
			service.generatePdf(mockPdfSaving);	
		} catch (HttpServerErrorException exception) {
			verify(mockPdf).update(PdfStatus.ERRO);
			return;
		}
		fail();
	}
	
	@Test
	@DisplayName("generatePdf: microsserviço retorna código de status não suportado")
	void cenario6() throws IOException {
		when(mockRepository.getReferenceById(anyLong())).thenReturn(mockPdf);
		
		@SuppressWarnings("unchecked")
		ResponseEntity<byte[]> mockResponse = mock(ResponseEntity.class);
		when(mockMicroserviceClient.generatePdf(any())).thenReturn(mockResponse);
		
		when(mockResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(300));
		
		PdfSaving mockPdfSaving = mock(PdfSaving.class);
		
		try {
			service.generatePdf(mockPdfSaving);	
		} catch (UnsupportedHttpStatusException exception) {
			verify(mockPdf).update(PdfStatus.ERRO);
			return;
		}
		fail();
	}
	
	@Test
	@DisplayName("previewPdf: microsserviço retorna 2xx")
	void cenario7() {
		@SuppressWarnings("unchecked")
		ResponseEntity<byte[]> mockResponse = mock(ResponseEntity.class);
		when(mockMicroserviceClient.previewPdf(any())).thenReturn(mockResponse);
		
		byte[] expectedResponseBody = {1, 2, 3};
		when(mockResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
		when(mockResponse.getBody()).thenReturn(expectedResponseBody);
		
		MicroserviceRequest mockMicroserviceRequest = mock(MicroserviceRequest.class);
		byte[] responseBody = service.previewPdf(mockMicroserviceRequest);
		
		assertArrayEquals(expectedResponseBody, responseBody);
	}
	
	@Test
	@DisplayName("previewPdf: microsserviço retorna 4xx")
	void cenario8() {
		@SuppressWarnings("unchecked")
		ResponseEntity<byte[]> mockResponse = mock(ResponseEntity.class);
		when(mockMicroserviceClient.previewPdf(any())).thenReturn(mockResponse);
		
		when(mockResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(400));
		
		MicroserviceRequest mockMicroserviceRequest = mock(MicroserviceRequest.class);
		
		assertThrows(HttpClientErrorException.class, () -> service.previewPdf(mockMicroserviceRequest));
	}
	
	@Test
	@DisplayName("previewPdf: microsserviço retorna 5xx")
	void cenario9() {
		@SuppressWarnings("unchecked")
		ResponseEntity<byte[]> mockResponse = mock(ResponseEntity.class);
		when(mockMicroserviceClient.previewPdf(any())).thenReturn(mockResponse);
		
		when(mockResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(500));
		
		MicroserviceRequest mockMicroserviceRequest = mock(MicroserviceRequest.class);
		
		assertThrows(HttpServerErrorException.class, () -> service.previewPdf(mockMicroserviceRequest));
	}
	
	@Test
	@DisplayName("previewPdf: microsserviço retorna código de status não suportado")
	void cenario10() {
		@SuppressWarnings("unchecked")
		ResponseEntity<byte[]> mockResponse = mock(ResponseEntity.class);
		when(mockMicroserviceClient.previewPdf(any())).thenReturn(mockResponse);
		
		when(mockResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(300));
		
		MicroserviceRequest mockMicroserviceRequest = mock(MicroserviceRequest.class);
		
		assertThrows(UnsupportedHttpStatusException.class, () -> service.previewPdf(mockMicroserviceRequest));
	}
}