package com.systextil.relatorio.record;

import com.systextil.relatorio.entity.SavedQuery;

public record SavedQueryListing(
        String queryName,
        String query
) {
    public SavedQueryListing(SavedQuery savedQuery) {
        this(
                savedQuery.getQueryName(),
                savedQuery.getQuery()
        );
    }

}
