package com.systextil.relatorio.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    @Query("SELECT c FROM Cliente c")
    public List<Cliente> findPrimeiro();
}
