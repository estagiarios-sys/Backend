package com.systextil.relatorio.domain.pdf;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

record PdfSaving(
	@NotNull
	@Positive
	Long pdfId,
	@NotBlank
	String fullTableHTML,
	@NotNull
	String imgPDF
) {}