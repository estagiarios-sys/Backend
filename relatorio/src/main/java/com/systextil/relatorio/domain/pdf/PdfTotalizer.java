package com.systextil.relatorio.domain.pdf;

import java.util.Map;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Embeddable
public class PdfTotalizer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String totalizerColumn;
	private String name;
	@ManyToOne
	@JoinColumn(name = "pdf_id")
	private Pdf pdf;
	
	public PdfTotalizer() {}
	
	public PdfTotalizer(PdfTotalizerSaving pdfTotalizerSaving) {
		for (Map.Entry<String, String> totalizer : pdfTotalizerSaving.totalizer().entrySet()) {
			this.totalizerColumn = totalizer.getKey();
			this.name = totalizer.getValue();
		}
	}

	public Long getId() {
		return id;
	}

	public String getTotalizerColumn() {
		return totalizerColumn;
	}

	public String getName() {
		return name;
	}

	public Pdf getPdf() {
		return pdf;
	}
}