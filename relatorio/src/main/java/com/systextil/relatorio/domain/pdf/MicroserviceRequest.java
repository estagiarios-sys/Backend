package com.systextil.relatorio.domain.pdf;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record MicroserviceRequest(
	@NotBlank
	String fullTableHTML,
	@NotNull
	String titlePDF,
	@NotNull
	String imgPDF
) {}