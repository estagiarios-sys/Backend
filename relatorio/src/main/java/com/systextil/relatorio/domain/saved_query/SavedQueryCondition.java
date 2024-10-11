package com.systextil.relatorio.domain.saved_query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class SavedQueryCondition {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String conditionName;
	
	@ManyToOne
	private SavedQuery savedQuery;
	
	public SavedQueryCondition() {}

	public SavedQueryCondition(String condition) {
		this.conditionName = condition;
	}
	
	public Long getId() {
		return id;
	}

	public String getConditionName() {
		return conditionName;
	}

	public SavedQuery getSavedQuery() {
		return savedQuery;
	}
	
	

}
