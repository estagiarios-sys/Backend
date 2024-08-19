package com.systextil.relatorio.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    @Query(":colunas")
    public Object[] findClientes( String colunas);
}
