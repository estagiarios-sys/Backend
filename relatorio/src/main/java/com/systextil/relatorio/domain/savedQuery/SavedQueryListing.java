package com.systextil.relatorio.domain.savedQuery;

record SavedQueryListing(
        String queryName,
        String query
) {
    SavedQueryListing(SavedQuery savedQuery) {
        this(
                savedQuery.getQueryName(),
                savedQuery.getQuery()
        );
    }

}
