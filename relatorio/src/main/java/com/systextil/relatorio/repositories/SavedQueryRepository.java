package com.systextil.relatorio.repositories;

import com.systextil.relatorio.entity.SavedQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedQueryRepository extends JpaRepository<SavedQuery, Long> {
}
