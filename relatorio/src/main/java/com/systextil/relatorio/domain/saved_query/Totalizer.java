package com.systextil.relatorio.domain.saved_query;

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
public class Totalizer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String totalizerColumn;
	private String name;
	@ManyToOne
	@JoinColumn(name = "saved_query_id")
	private SavedQuery savedQuery;
	
	public Totalizer() {
	}
	
	public Totalizer(TotalizerSaving totalizerSaving) {
		for (Map.Entry<String, String> totalizer : totalizerSaving.totalizer().entrySet()) {
			this.totalizerColumn = totalizer.getKey();
			this.name = totalizer.getValue();
		}
	}

	public Long getId() {
		return id;
	}

	public String getColumn() {
		return totalizerColumn;
	}
	
	public String getName() {
		return name;
	}

	public SavedQuery getSavedQuery() {
		return savedQuery;
	}
}