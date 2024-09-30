package com.systextil.relatorio.domain.pdf;

import java.time.LocalDateTime;

record PdfListing(
		Long id,
		String pdfTitle,
		LocalDateTime requestTime,
		LocalDateTime generatedPdfTime,
		String status
) {
	
	public PdfListing(Pdf pdf) {
		this(
			pdf.getId(),
			pdf.getPdfTitle(),
			pdf.getRequestTime(),
			pdf.getGeneratedPdfTime(),
			pdf.getStatus()
		);
	}
	
}