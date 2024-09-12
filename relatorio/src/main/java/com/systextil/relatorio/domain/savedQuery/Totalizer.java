package com.systextil.relatorio.domain.savedQuery;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
@Embeddable
public class Totalizer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String totalizer;
	@ManyToOne
	private SavedQuery savedQuery;
	
	public Totalizer() {
	}
	
	public Totalizer(TotalizerSaving totalizerSaving) {
		this.totalizer = totalizerSaving.totalizer();
	}

	public Long getId() {
		return id;
	}

	public String getTotalizer() {
		return totalizer;
	}

	public SavedQuery getSavedQuery() {
		return savedQuery;
	}
	
}
