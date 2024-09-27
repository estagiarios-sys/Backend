package com.systextil.relatorio.domain.pdf;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

record PdfSaving(
		@NotBlank
		String fullTableHTML,
	    String titlePDF,
	    String imgPDF,
	    @NotBlank
		String finalQuery,
		String totalizersQuery,
		List<PdfTotalizerSaving> totalizers
) {}