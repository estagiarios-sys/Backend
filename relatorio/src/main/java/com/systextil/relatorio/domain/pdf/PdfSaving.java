package com.systextil.relatorio.domain.pdf;

record PdfSaving(
		String fullTableHTML,
	    String titlePDF,
	    String imgPDF,
		String finalQuery,
		String totalizersQuery		
) {}