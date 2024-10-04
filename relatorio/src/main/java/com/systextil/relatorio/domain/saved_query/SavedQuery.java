package com.systextil.relatorio.domain.saved_query;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "consultas_salvas")
public class SavedQuery {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String queryName;
    private String finalQuery;
    private String totalizersQuery;
    @Column(name = "IMAGE_PDF")
    private byte[] imagePDF;
    @Column(name = "TITLE_PDF")
    private String titlePDF;
    
    @OneToMany(cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    @Embedded
    @JoinColumn(name = "saved_query_id")
    private List<Totalizer> totalizers;

    public SavedQuery() {
    }
    
    public SavedQuery(SavedQuerySaving savedQuerySaving, byte[] imagePDF) {
    	this.queryName = savedQuerySaving.queryName();
    	this.finalQuery = savedQuerySaving.finalQuery();
    	this.totalizersQuery = savedQuerySaving.totalizersQuery();
    	this.imagePDF = imagePDF;
        this.titlePDF = savedQuerySaving.titlePDF();
    	try {
    		this.totalizers = savedQuerySaving.totalizers().stream().map(Totalizer::new).toList();
    	} catch(NullPointerException e) {
    		this.totalizers = null;
    	}
    }

    public Long getId() {
        return id;
    }

    public String getQueryName() {
        return queryName;
    }

    public String getFinalQuery() {
        return finalQuery;
    }
    
    public String getTotalizersQuery() {
		return totalizersQuery;
	}

    public byte[] getImagePDF(){
        return imagePDF;
    }
	
    public String getTitlePDF(){
        return titlePDF;
    }

    public List<Totalizer> getTotalizers() {
		return totalizers;
	}    
    
    public void updateData(SavedQuerySaving savedQuerySaving, byte[] imagePDF) {
    	this.finalQuery = savedQuerySaving.finalQuery();
    	this.totalizersQuery = savedQuerySaving.totalizersQuery();
    	this.imagePDF = imagePDF;
    	try {
    		this.totalizers = savedQuerySaving.totalizers().stream().map(Totalizer::new).toList();
    	} catch(NullPointerException exception) {
    		this.totalizers = null;
    	}
    }
}