package com.systextil.relatorio.domain.saved_query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class SavedQueryTotalizer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String totalizerColumn;
	private String totalizerType;
	
	@ManyToOne
	private SavedQuery savedQuery;
	
	public SavedQueryTotalizer() {}

	public SavedQueryTotalizer(String column, String type) {
		this.totalizerColumn = column;
		this.totalizerType = type;
	}
	
	public Long getId() {
		return id;
	}

	public String getTotalizerColumn() {
		return totalizerColumn;
	}

	public String getTotalizerType() {
		return totalizerType;
	}

	public SavedQuery getSavedQuery() {
		return savedQuery;
	}
}