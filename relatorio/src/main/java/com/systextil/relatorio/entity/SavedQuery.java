package com.systextil.relatorio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "consultas_salvas")
public class SavedQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String queryName;
    private String query;

    public SavedQuery() {
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
