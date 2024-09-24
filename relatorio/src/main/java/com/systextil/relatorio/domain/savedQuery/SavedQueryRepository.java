package com.systextil.relatorio.domain.savedQuery;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

interface SavedQueryRepository extends JpaRepository<SavedQuery, Long> {
	@Transactional
	void deleteByQueryName(String queryName);
	
	Optional<SavedQuery> findByQueryName(String queryName);
}