package com.systextil.relatorio.domain.pdf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

interface PdfRepository extends JpaRepository<Pdf, Long> {
	
	@Query("SELECT new com.systextil.relatorio.domain.pdf.PdfListing(p.id, p.pdfTitle, p.requestTime, p.generatedPdfTime, p.status) FROM Pdf p ORDER BY p.id DESC")
    List<PdfListing> findAttributesForList();
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT MIN(p.id) FROM Pdf p")
	Long getOldestEntry();
	
	@Query("SELECT p.path FROM Pdf p WHERE p.id = :id")
	String findPathById(@Param("id") Long id);
}