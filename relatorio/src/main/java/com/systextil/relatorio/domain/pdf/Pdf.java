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
	private String path;
	private String status;
	
	public Pdf() {
	}
	
	public Pdf(String pdfTitle, LocalDateTime requestTime) {
		this.pdfTitle = pdfTitle;
		this.requestTime = requestTime;
		this.generatedPdfTime = null;
		this.path = null;
		this.status = PdfStatus.BUSCANDO_DADOS.toString();
	}
	
	public void update(LocalDateTime generatedPdfTime, String path) {
		this.generatedPdfTime = generatedPdfTime;
		this.path = path;
		this.status = PdfStatus.CONCLUIDO.toString();
	}
	
	public void update(PdfStatus status) {
		this.status = status.toString();
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

	public LocalDateTime getGeneratedPdfTime() {
		return generatedPdfTime;
	}

	public String getPath() {
		return path;
	}

	public String getStatus() {
		return status;
	}
}