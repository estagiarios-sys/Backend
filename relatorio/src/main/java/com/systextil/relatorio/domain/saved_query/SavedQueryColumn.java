package com.systextil.relatorio.domain.saved_query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class SavedQueryColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String columnName;
	
	@ManyToOne
	private SavedQuery savedQuery;

	public SavedQueryColumn() {}
	
	public SavedQueryColumn(String column) {
		this.columnName = column;
	}
	
	public Long getId() {
		return id;
	}

	public String getColumnName() {
		return columnName;
	}

	public SavedQuery getSavedQuery() {
		return savedQuery;
	}
}