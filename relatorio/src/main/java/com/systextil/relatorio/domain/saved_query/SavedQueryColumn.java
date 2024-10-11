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
	private String columnNickName;
	private String columnType;
	
	@ManyToOne
	private SavedQuery savedQuery;

	public SavedQueryColumn() {}
	
	public SavedQueryColumn(SavedQueryColumnSavingListingAndUpdating saving) {
		this.columnName = saving.name();
		this.columnNickName = saving.nickName();
		this.columnType = saving.type();
	}
	
	public Long getId() {
		return id;
	}

	public String getColumnName() {
		return columnName;
	}
	
	public String getColumnNickName() {
		return columnNickName;
	}

	public String getColumnType() {
		return columnType;
	}

	public SavedQuery getSavedQuery() {
		return savedQuery;
	}
}