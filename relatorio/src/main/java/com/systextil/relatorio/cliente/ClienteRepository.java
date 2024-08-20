package com.systextil.relatorio.cliente;

import com.systextil.relatorio.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

}
