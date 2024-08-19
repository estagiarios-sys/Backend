package com.systextil.relatorio.cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Cliente {

    @Id
    private String cpf;
    private String nome;
    private String cep;

    @Column(name = "DATA_NASCIMENTO")
    private LocalDate dataNascimento;

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getCep() {
        return cep;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}
