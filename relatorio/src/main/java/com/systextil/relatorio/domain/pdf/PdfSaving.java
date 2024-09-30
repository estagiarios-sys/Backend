package com.systextil.relatorio.domain.pdf;

record PdfSaving(
		Long pdfId,
		String fullTableHTML,
	    String imgPDF
) {}