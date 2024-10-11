package com.systextil.relatorio.domain.saved_query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class SavedQueryJoin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String joinName;
	
	@ManyToOne
	private SavedQuery savedQuery;

	public SavedQueryJoin() {}
	
	public SavedQueryJoin(String join) {
		this.joinName = join;
	}
	
	public Long getId() {
		return id;
	}

	public String getJoinName() {
		return joinName;
	}

	public SavedQuery getSavedQuery() {
		return savedQuery;
	}
}