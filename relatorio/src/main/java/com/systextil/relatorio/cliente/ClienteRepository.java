package com.systextil.relatorio.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    ArrayList<Object[]> findClientesByColumns(String colunas, String tabela) throws SQLException, ClassNotFoundException;

}
