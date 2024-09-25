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
    @Column(name = "IMG_PDF")
    private byte[] imgPDF;
    @Column(name = "TITLE_PDF")
    private String titlePDF;
    
    @OneToMany(cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    @Embedded
    @JoinColumn(name = "saved_query_id")
    private List<Totalizer> totalizers;

    public SavedQuery() {
    }
    
    public SavedQuery(SavedQuerySaving savedQuerySaving, byte[] imgPDF) {
    	this.queryName = savedQuerySaving.queryName();
    	this.finalQuery = savedQuerySaving.finalQuery();
    	this.totalizersQuery = savedQuerySaving.totalizersQuery();
    	this.imgPDF = imgPDF;
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

    public byte[] getImgPDF(){
        return imgPDF;
    }
	
    public String getTitlePDF(){
        return titlePDF;
    }

    public List<Totalizer> getTotalizers() {
		return totalizers;
	}    
    
    public void updateData(SavedQuerySaving savedQuerySaving, byte[] imgPDF) {
    	this.finalQuery = savedQuerySaving.finalQuery();
    	this.totalizersQuery = savedQuerySaving.totalizersQuery();
    	this.imgPDF = imgPDF;
    	try {
    		this.totalizers = savedQuerySaving.totalizers().stream().map(Totalizer::new).toList();
    	} catch(NullPointerException exception) {
    		this.totalizers = null;
    	}
    }
}