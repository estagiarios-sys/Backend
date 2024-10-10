package com.systextil.relatorio.domain.saved_query;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class SavedQuery {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String queryName;
    private String mainTable;
    private String conditions;
    private String orderBy;
    private String pdfTitle;
    private byte[] pdfImage;
    
    @OneToMany(cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    @Embedded
    @JoinColumn(name = "saved_query_id")
    private List<SavedQueryColumn> savedQueryColumns;
    
    @OneToMany(cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    @Embedded
    @JoinColumn(name = "saved_query_id")
    private List<SavedQueryJoin> savedQueryJoins;
    
    @OneToMany(cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    @Embedded
    @JoinColumn(name = "saved_query_id")
    private List<SavedQueryTotalizer> savedQueryTotalizers;

    public SavedQuery() {
    }
    
    public SavedQuery(SavedQuerySaving savedQuerySaving, byte[] pdfImage) {
    	this.queryName = savedQuerySaving.queryName();
    	this.mainTable = savedQuerySaving.mainTable();
    	this.conditions = savedQuerySaving.conditions();
    	this.orderBy = savedQuerySaving.orderBy();
    	this.pdfTitle = savedQuerySaving.pdfTitle();
    	this.pdfImage = pdfImage;
    	this.savedQueryColumns = savedQuerySaving.columns()
    			.stream()
    			.map(SavedQueryColumn::new)
    			.toList();
    	this.savedQueryJoins = savedQuerySaving.joins()
    			.stream()
    			.map(SavedQueryJoin::new)
    			.toList();
    	this.savedQueryTotalizers = savedQuerySaving.totalizers()
    			.stream()
    			.map(SavedQueryTotalizer::new)
    			.toList();
    }
    
    public void updateData(SavedQueryUpdating updating, byte[] pdfImage) {
    	this.mainTable = updating.mainTable();
    	this.conditions = updating.conditions();
    	this.orderBy = updating.orderBy();
    	this.pdfTitle = updating.pdfTitle();
    	this.pdfImage = pdfImage;
    	this.savedQueryColumns = updating.columns()
    			.stream()
    			.map(SavedQueryColumn::new)
    			.toList();
    	this.savedQueryJoins = updating.joins()
    			.stream()
    			.map(SavedQueryJoin::new)
    			.toList();
    	this.savedQueryTotalizers = updating.totalizers()
    			.stream()
    			.map(SavedQueryTotalizer::new)
    			.toList();
    }

	public Long getId() {
		return id;
	}

	public String getQueryName() {
		return queryName;
	}

	public String getMainTable() {
		return mainTable;
	}

	public String getConditions() {
		return conditions;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public String getPdfTitle() {
		return pdfTitle;
	}

	public byte[] getPdfImage() {
		return pdfImage;
	}

	public List<SavedQueryColumn> getSavedQueryColumns() {
		return savedQueryColumns;
	}

	public List<SavedQueryJoin> getSavedQueryJoins() {
		return savedQueryJoins;
	}

	public List<SavedQueryTotalizer> getSavedQueryTotalizers() {
		return savedQueryTotalizers;
	}
}