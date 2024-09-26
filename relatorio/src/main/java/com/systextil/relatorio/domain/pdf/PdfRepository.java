package com.systextil.relatorio.domain.pdf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface PdfRepository extends JpaRepository<Pdf, Long> {
	
	@Query("SELECT new com.systextil.relatorio.domain.pdf.PdfListing(p.id, p.pdfTitle, p.requestTime, p.generatedPdfTime) FROM Pdf p")
    List<PdfListing> findAttributesForList();
	
	//byte[] findBodyById(Long id);
}