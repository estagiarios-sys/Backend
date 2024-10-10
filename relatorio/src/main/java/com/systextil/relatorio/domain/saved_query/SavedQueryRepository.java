package com.systextil.relatorio.domain.saved_query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface SavedQueryRepository extends JpaRepository<SavedQuery, Long> {
	@Query("SELECT new com.systextil.relatorio.domain.saved_query.AllSavedQueriesListing(sv.id, sv.queryName) FROM SavedQuery sv")
	List<AllSavedQueriesListing> findAllForListing();
}