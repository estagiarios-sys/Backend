package com.systextil.relatorio.entity;

import java.util.ArrayList;

public class Tabela {

    private String nome;
    private String pk;
    private String fk;
    private String orderBy;
    private ArrayList<String> condicoes;
    private ArrayList<String> colunas;

    public Tabela(
            String nome,
            String orderBy,
            ArrayList<String> condicoes,
            ArrayList<String> colunas
    ) {
        this.nome = nome;
        this.pk = null;
        this.fk = null;
        this.orderBy = orderBy;
        this.condicoes = condicoes;
        this.colunas = colunas;
    }

    public String getNome() {
        return nome;
    }

    public String getPk() {
        return pk;
    }

    private String getFk() {
        return fk;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public ArrayList<String> getCondicoes() {
        return condicoes;
    }

    public ArrayList<String> getColunas() {
        return colunas;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
}
