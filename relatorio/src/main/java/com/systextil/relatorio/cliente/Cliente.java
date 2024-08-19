package com.systextil.relatorio.cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Cliente {

    @Id
    private String cpf;
    private String nome;
    private String cep;
    private LocalDate dataNascimento;

}
