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
    private String orderBy;
    private String pdfTitle;
    private byte[] pdfImage;
    
    @OneToMany(cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    @JoinColumn(name = "saved_query_id")
    private List<SavedQueryColumn> savedQueryColumns;
    
    @OneToMany(cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    @JoinColumn(name = "saved_query_id")
    private List<SavedQueryCondition> savedQueryConditions; 
    
    @OneToMany(cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    @JoinColumn(name = "saved_query_id")
    private List<SavedQueryJoin> savedQueryJoins;
    
    @OneToMany(cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    @JoinColumn(name = "saved_query_id")
    private List<SavedQueryTotalizer> savedQueryTotalizers;

    public SavedQuery() {
    }
    
    public SavedQuery(SavedQuerySaving saving, byte[] pdfImage) {
    	this.queryName = saving.queryName();
    	this.mainTable = saving.table();
    	this.orderBy = saving.orderBy();
    	this.pdfTitle = saving.pdfTitle();
    	this.pdfImage = pdfImage;
    	this.savedQueryColumns = saving.columns()
    			.stream()
    			.map(SavedQueryColumn::new)
    			.toList();
    	this.savedQueryConditions = saving.conditions()
    			.stream()
    			.map(SavedQueryCondition::new)
    			.toList();
    	this.savedQueryJoins = saving.tablesPairs()
    			.stream()
    			.map(SavedQueryJoin::new)
    			.toList();
    	this.savedQueryTotalizers = saving.totalizers()
    			.stream()
    			.map(SavedQueryTotalizer::new)
    			.toList();
    }
    
    public void updateData(SavedQueryUpdating updating, byte[] pdfImage) {
    	this.mainTable = updating.mainTable();
    	this.orderBy = updating.orderBy();
    	this.pdfTitle = updating.pdfTitle();
    	this.pdfImage = pdfImage;
    	this.savedQueryColumns = updating.columns()
    			.stream()
    			.map(SavedQueryColumn::new)
    			.toList();
    	this.savedQueryConditions = updating.conditions()
    			.stream()
    			.map(SavedQueryCondition::new)
    			.toList();
    	this.savedQueryJoins = updating.tablesPairs()
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

	public List<SavedQueryCondition> getSavedQueryConditions() {
		return savedQueryConditions;
	}
	
	public List<SavedQueryJoin> getSavedQueryJoins() {
		return savedQueryJoins;
	}

	public List<SavedQueryTotalizer> getSavedQueryTotalizers() {
		return savedQueryTotalizers;
	}
}