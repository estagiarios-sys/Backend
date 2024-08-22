package com.systextil.relatorio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "consultas_salvas")
public class SaveQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String queryName;
    private String query;

    public SaveQuery() {
    }

    public SaveQuery(String queryName, String query) {
        this.queryName = queryName;
        this.query = query;
    }

    public Long getId() {
        return id;
    }

    public String getQueryName() {
        return queryName;
    }

    public String getQuery() {
        return query;
    }
}
