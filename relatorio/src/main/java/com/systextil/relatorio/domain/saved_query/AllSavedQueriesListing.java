package com.systextil.relatorio.domain.saved_query;

record AllSavedQueriesListing(
	Long id,
	String queryName
) {
	AllSavedQueriesListing(SavedQuery savedQuery) {
		this(
			savedQuery.getId(),
			savedQuery.getQueryName()
		);
	}
}