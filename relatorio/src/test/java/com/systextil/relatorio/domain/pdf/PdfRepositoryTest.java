package com.systextil.relatorio.domain.pdf;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PdfRepositoryTest {

	@Autowired
	private PdfRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	@DisplayName("getOldestEntry")
	void cenario1() {
		Pdf pdfMaisNovo = new Pdf("pdf mais novo", LocalDateTime.now());
		Pdf pdfDoMeio = new Pdf("pdf do meio", LocalDateTime.now());
		Pdf pdfMaisVelho = new Pdf("pdf mais velho", LocalDateTime.now());
		
		Pdf pdfEsperado = entityManager.persist(pdfMaisNovo);
		entityManager.persist(pdfDoMeio);
		entityManager.persist(pdfMaisVelho);
		
		Long id = repository.getOldestEntry();
		
		assertEquals(pdfEsperado.getId(), id);
	}
}