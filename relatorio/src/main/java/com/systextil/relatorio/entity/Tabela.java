package com.systextil.relatorio.entity;

import java.util.ArrayList;

public class Tabela {

    private String nome;
    private ArrayList<String> colunas;
    private ArrayList<String> condicoes;
    private String orderBy;
    private String pk;
    private String fk;

    public Tabela() {
    }

    public Tabela(
            String nome,
            ArrayList<String> condicoes,
            ArrayList<String> colunas,
            String orderBy
    ) {
        this.nome = nome;
        this.colunas = colunas;
        this.condicoes = condicoes;
        this.orderBy = orderBy;
        this.pk = null;
        this.fk = null;
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
    public void setFk(String fk) {
        this.fk = fk;
    }
}
