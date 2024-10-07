package com.systextil.relatorio.domain.saved_query;

import java.util.Map;

import com.systextil.relatorio.domain.ColumnAndTotalizer;
import com.systextil.relatorio.domain.TotalizerTypes;

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

	public SavedQueryTotalizer(ColumnAndTotalizer totalizer) {
		for (Map.Entry<String, TotalizerTypes> columnAndTotalizer : totalizer.totalizer().entrySet()) {
			this.totalizerColumn = columnAndTotalizer.getKey();
			this.totalizerType = columnAndTotalizer.getValue().toString();
		}
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