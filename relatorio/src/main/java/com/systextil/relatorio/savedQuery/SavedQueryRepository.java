package com.systextil.relatorio.savedQuery;

import org.springframework.data.jpa.repository.JpaRepository;

interface SavedQueryRepository extends JpaRepository<SavedQuery, Long> {
}
