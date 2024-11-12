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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class PdfServiceTest {

	@Autowired
	private PdfService service;
	
	@MockBean
	private PdfRepository repository;
	
	@BeforeAll
	void setUp() {
		mockStatic(LocalDateTime.class);
		mockStatic(Files.class);
		mockStatic(Paths.class);
	}
	
	@AfterAll
	void clean() {
		Mockito.clearAllCaches();
	}
	
	@Test
	@DisplayName("createNoDataPdf: tem menos de 10 pdfs no banco")
	void cenario1() throws IOException {
		when(repository.count()).thenReturn(9L);
		Pdf mockPdf = mock(Pdf.class);
		
		when(repository.save(any())).thenReturn(mockPdf);
		when(mockPdf.getId()).thenReturn(1L);
		
		Long id = service.createNoDataPdf("Título");
		
		verify(repository, never()).deleteById(anyLong());
		
		assertEquals(1L, id);
	}

}