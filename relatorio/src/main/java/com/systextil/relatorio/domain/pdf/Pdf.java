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
	private byte[] body;
	
	public Pdf() {
	}
	
	public Pdf(String pdfTitle, LocalDateTime requestTime, LocalDateTime generatedPdfTime, byte[] body) {
		this.pdfTitle = pdfTitle;
		this.requestTime = requestTime;
		this.generatedPdfTime = generatedPdfTime;
		this.body = body;
	}
	
	public Pdf(Long id, String pdfTitle, LocalDateTime requestTime, LocalDateTime generatedPdfTime) {
		this.id = id;
		this.pdfTitle = pdfTitle;
		this.requestTime = requestTime;
		this.generatedPdfTime = generatedPdfTime;
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

	public byte[] getBody() {
		return body;
	}	
}