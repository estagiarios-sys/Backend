package com.systextil.relatorio.repositories;

import com.systextil.relatorio.entity.SaveQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaSalvaRepository extends JpaRepository<SaveQuery, Long> {
}
