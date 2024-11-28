package com.systextil.relatorio.domain.pdf;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record PdfSaving(
		@NotBlank
		Long pdfId,
		@NotBlank
		String fullTableHTML,
		@NotNull
	    String imgPDF
) {}