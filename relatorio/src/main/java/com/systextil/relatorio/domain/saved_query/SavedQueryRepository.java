package com.systextil.relatorio.domain.saved_query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

interface SavedQueryRepository extends JpaRepository<SavedQuery, Long> {
	@Query("""
			SELECT
				new com.systextil.relatorio.domain.saved_query.AllSavedQueriesListing(sv.id, sv.queryName)
			FROM
				SavedQuery sv
			""")
	List<AllSavedQueriesListing> findAllForListing();
	
	Optional<SavedQuery> findByQueryName(String queryName);
	
	@Transactional
	void deleteByQueryName(String queryName);
}