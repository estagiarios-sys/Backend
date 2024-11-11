package com.systextil.relatorio.domain.pdf;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Classe configurada para usar o banco em memória H2
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@TestInstance(Lifecycle.PER_CLASS)
class PdfRepositoryTest {

	@Autowired
	private PdfRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	private Long stId = 1L;
	private Long ndId = 2L;
	private Long rdId = 3L;
	
	@BeforeEach
	void setUp() {
		Pdf newestPdf = new Pdf("pdf mais novo", LocalDateTime.of(1, 1, 1, 1, 1));
		Pdf middlePdf = new Pdf("pdf do meio", LocalDateTime.of(2, 2, 2, 2, 2));
		Pdf oldestPdf = new Pdf("pdf mais velho", LocalDateTime.of(3, 3, 3, 3, 3));
		
		newestPdf.update(LocalDateTime.of(1, 1, 1, 1, 1), "caminho1");
		middlePdf.update(LocalDateTime.of(2, 2, 2, 2, 2), "caminho2");
		oldestPdf.update(LocalDateTime.of(3, 3, 3, 3, 3), "caminho3");
		
		entityManager.persist(newestPdf);
		entityManager.persist(middlePdf);
		entityManager.persist(oldestPdf);
	}
	
	@AfterEach
	void incrementId() {
		stId += 3L;
		ndId += 3L;
		rdId += 3L;
	}
	
	@Test
	@DisplayName("findAttributesForList")
	void cenario1() {
		List<PdfListing> pdfsList = repository.findAttributesForList();
		PdfListing firstExpectedPdf = new PdfListing(rdId, "pdf mais velho", LocalDateTime.of(3, 3, 3, 3, 3), LocalDateTime.of(3, 3, 3, 3, 3), "CONCLUIDO");
		PdfListing secondExpectedPdf = new PdfListing(ndId, "pdf do meio", LocalDateTime.of(2, 2, 2, 2, 2), LocalDateTime.of(2, 2, 2, 2, 2), "CONCLUIDO");
		PdfListing thirdExpectedPdf = new PdfListing(stId, "pdf mais novo", LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "CONCLUIDO");
		
		assertEquals(List.of(firstExpectedPdf, secondExpectedPdf, thirdExpectedPdf), pdfsList);
	}
	
	@Test
	@DisplayName("getOldestEntry")
	void cenario2() {
		Long id = repository.getOldestEntry();
		
		assertEquals(stId, id);
	}
	
	@Test
	@DisplayName("findPathById")
	void cenario3() {
		String path = repository.findPathById(stId);
		
		assertEquals("caminho1", path);
	}
}