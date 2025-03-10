package com.systextil.relatorio.domain.pdf;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

/**
 * Classe configurada para usar o banco em memória H2
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class PdfRepositoryTest {

	@Autowired
	private PdfRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@BeforeEach
	void setUp() {
		Pdf oldestPdf = new Pdf("pdf mais velho", LocalDateTime.of(1, 1, 1, 1, 1));
		Pdf middlePdf = new Pdf("pdf do meio", LocalDateTime.of(2, 2, 2, 2, 2));
		Pdf newestPdf = new Pdf("pdf mais novo", LocalDateTime.of(3, 3, 3, 3, 3));

		oldestPdf.update(LocalDateTime.of(1, 1, 1, 1, 1), "caminho1");
		middlePdf.update(LocalDateTime.of(2, 2, 2, 2, 2), "caminho2");
		newestPdf.update(LocalDateTime.of(3, 3, 3, 3, 3), "caminho3");
		
		entityManager.persist(oldestPdf);
		entityManager.persist(middlePdf);
		entityManager.persist(newestPdf);
	}
	
	@Test
	@DisplayName("findAttributesForList")
	void cenario1() {
		List<PdfListing> pdfsList = repository.findAttributesForList();
		
		PdfListing firstExpectedPdf = new PdfListing(3L, "pdf mais novo", LocalDateTime.of(3, 3, 3, 3, 3), LocalDateTime.of(3, 3, 3, 3, 3), "CONCLUIDO");
		PdfListing secondExpectedPdf = new PdfListing(2L, "pdf do meio", LocalDateTime.of(2, 2, 2, 2, 2), LocalDateTime.of(2, 2, 2, 2, 2), "CONCLUIDO");
		PdfListing thirdExpectedPdf = new PdfListing(1L, "pdf mais velho", LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "CONCLUIDO");
		
		assertEquals(List.of(firstExpectedPdf, secondExpectedPdf, thirdExpectedPdf), pdfsList);
	}
	
	@Test
	@DisplayName("getOldestEntry")
	void cenario2() {
		Long id = repository.getOldestEntry();
		
		assertEquals(1L, id);
	}
	
	@Test
	@DisplayName("findPathById")
	void cenario3() {
		String path = repository.findPathById(1L);
		
		assertEquals("caminho1", path);
	}
}