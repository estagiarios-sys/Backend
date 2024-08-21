package com.systextil.relatorio.entity;

import java.util.ArrayList;

public class Tabela {

    private String nome;
    private ArrayList<String> colunas;
    private ArrayList<String> condicoes;
    private String orderBy;
    private  ArrayList<String> join;

    public Tabela() {
    }

    public Tabela(
            String nome,
            ArrayList<String> condicoes,
            ArrayList<String> colunas,
            String orderBy,
            ArrayList<String> join
    ) {
        this.nome = nome;
        this.colunas = colunas;
        this.condicoes = condicoes;
        this.orderBy = orderBy;
        this.join = join;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<String> getJoin() {
        return join;
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
}
