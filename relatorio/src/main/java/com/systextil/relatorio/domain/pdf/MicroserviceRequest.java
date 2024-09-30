package com.systextil.relatorio.domain.pdf;

record MicroserviceRequest(
	String fullTableHTML,
	String titlePDF,
	String imgPDF
) {}