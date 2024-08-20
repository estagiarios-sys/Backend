package com.systextil.relatorio.entity;

import java.util.ArrayList;

public class Relacionamento {

    private Tabela tabelaPrincipal;
    private ArrayList<Tabela> tabelasRelacionadas;

    public Relacionamento(Tabela tabelaPrincipal, ArrayList<Tabela> tabelasRelacionadas) {
        this.tabelaPrincipal = tabelaPrincipal;
        this.tabelasRelacionadas = tabelasRelacionadas;
    }

    public Tabela getTabelaPrincipal() {
        return tabelaPrincipal;
    }

    public ArrayList<Tabela> getTabelasRelacionadas() {
        return tabelasRelacionadas;
    }
}
