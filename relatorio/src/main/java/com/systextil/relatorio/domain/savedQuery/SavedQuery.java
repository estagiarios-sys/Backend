package com.systextil.relatorio.domain.savedQuery;

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
    private byte[] imgPDF;
    private String titlePDF;
    
    @OneToMany(cascade = CascadeType.PERSIST)
    @Embedded
    @JoinColumn(name = "saved_query_id")
    private List<Totalizer> totalizers;

    public SavedQuery() {
    }
    
    public SavedQuery(SavedQuerySaving savedQuerySaving) {
    	this.queryName = savedQuerySaving.queryName();
    	this.finalQuery = savedQuerySaving.finalQuery();
    	this.totalizersQuery = savedQuerySaving.totalizersQuery();
        this.titlePDF = savedQuerySaving.titlePDF();
        this.imgPDF = savedQuerySaving.imgPDF();
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

    public String getTitlePDF(){
        return titlePDF;
    }

    public byte[] getImgPDF(){
        return imgPDF;
    }

    public void setImgPDF(byte[] imgPDF){
        this.imgPDF = imgPDF;
    }

	public List<Totalizer> getTotalizers() {
		return totalizers;
	}    
    
    public void updateData(SavedQuerySaving savedQuerySaving) {
    	this.finalQuery = savedQuerySaving.finalQuery();
    	this.totalizersQuery = savedQuerySaving.totalizersQuery();
    	try {
    		this.totalizers = savedQuerySaving.totalizers().stream().map(t -> new Totalizer(t)).toList();
    	} catch(NullPointerException e) {
    		this.totalizers = null;
    	}
    	
    }


}