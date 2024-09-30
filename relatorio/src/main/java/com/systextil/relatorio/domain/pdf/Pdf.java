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
	private String status;
	
	public Pdf() {
	}
	
	public Pdf(String pdfTitle, LocalDateTime requestTime) {
		this.pdfTitle = pdfTitle;
		this.requestTime = requestTime;
		this.generatedPdfTime = null;
		this.body = null;
		this.status = StatusTypes.EM_ANDAMENTO.toString();
	}
	
	public void update(LocalDateTime generatedPdfTime, byte[] body) {
		this.generatedPdfTime = generatedPdfTime;
		this.body = body;
		this.status = StatusTypes.CONCLUIDO.toString();
	}
	
	public void update() {
		this.status = StatusTypes.ERRO.toString();
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

	public byte[] getBody() {
		return body;
	}

	public String getStatus() {
		return status;
	}
}