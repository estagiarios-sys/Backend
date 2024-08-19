package com.systextil.relatorio.notaFiscal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class NotaFiscal {

    @Id
    private Long numero;
    private LocalDate dataEmissao;
    private double valor;
    private String cpfCliente;

    public Long getNumero() {
        return numero;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public double getValor() {
        return valor;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }
}
