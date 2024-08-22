package com.systextil.relatorio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "consultas_salvas")
public class ConsultaSalva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeConsulta;
    private String sqlConsulta;

    public ConsultaSalva() {
    }

    public ConsultaSalva(String nomeConsulta, String sqlConsulta) {
        this.nomeConsulta = nomeConsulta;
        this.sqlConsulta = sqlConsulta;
    }

    public Long getId() {
        return id;
    }

    public String getNomeConsulta() {
        return nomeConsulta;
    }

    public String getSqlConsulta() {
        return sqlConsulta;
    }
}
