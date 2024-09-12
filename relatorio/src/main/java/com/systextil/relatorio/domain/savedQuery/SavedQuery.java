package com.systextil.relatorio.domain.savedQuery;

import java.util.ArrayList;

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
    @Embedded
    @OneToMany
    private ArrayList<Totalizer> totalizers;

    public SavedQuery() {
    }
    
    public SavedQuery(SavedQuerySaving savedQuerySaving) {
    	this.queryName = savedQuerySaving.queryName();
    	this.finalQuery = savedQuerySaving.finalQuery();
    	this.totalizersQuery = savedQuerySaving.totalizersQuery();
    	this.totalizers = (ArrayList<Totalizer>) savedQuerySaving.totalizers().stream().map(Totalizer::new).toList();
    }

    public Long getId() {
        return id;
    }

    public String getQueryName() {
        return queryName;
    }

    public String getQuery() {
        return finalQuery;
    }
    
	public String getTotalizersQuery() {
		return totalizersQuery;
	}

	public ArrayList<Totalizer> getTotalizers() {
		return totalizers;
	}    
    
    public void updateData(SavedQuerySaving savedQuerySaving) {
    	this.finalQuery = savedQuerySaving.finalQuery();
    	this.totalizersQuery = savedQuerySaving.totalizersQuery();
    	this.totalizers = (ArrayList<Totalizer>) savedQuerySaving.totalizers().stream().map(t -> new Totalizer(t)).toList();
    	
    }


}