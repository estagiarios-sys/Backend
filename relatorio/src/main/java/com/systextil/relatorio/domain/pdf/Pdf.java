package com.systextil.relatorio.domain.pdf;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pdf {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String pdfTitle;
	private LocalDateTime requestTime;
	private LocalDateTime generatedPdfTime;
	private String finalQuery;
	private String totalizersQuery;
	private byte[] body;
	
	public Pdf() {
	}
	
	public Pdf(PdfSaving pdfSaving, LocalDateTime requestTime, LocalDateTime generatedPdfTime, byte[] body) {
		this.pdfTitle = pdfSaving.titlePDF();
		this.requestTime = requestTime;
		this.generatedPdfTime = generatedPdfTime;
		this.finalQuery = pdfSaving.finalQuery();
		this.totalizersQuery = pdfSaving.totalizersQuery();
		this.body = body;
	}

	public Long getId() {
		return id;
	}

	public String getPdfTitle() {
		return pdfTitle;
	}

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public LocalDateTime getDataBaseSavingTime() {
		return generatedPdfTime;
	}

	public String getFinalQuery() {
		return finalQuery;
	}

	public String getTotalizersQuery() {
		return totalizersQuery;
	}

	public byte[] getBody() {
		return body;
	}	
}