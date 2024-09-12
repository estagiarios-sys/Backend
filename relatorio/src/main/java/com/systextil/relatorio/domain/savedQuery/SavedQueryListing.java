package com.systextil.relatorio.domain.savedQuery;

import java.util.List;

record SavedQueryListing(
        String queryName,
        String finalQuery,
        String totalizersQuery,
        List<TotalizerListing> totalizers
) {
    SavedQueryListing(SavedQuery savedQuery) {
        this(
                savedQuery.getQueryName(),
                savedQuery.getFinalQuery(),
                savedQuery.getTotalizersQuery(),
                savedQuery.getTotalizers().stream().map(TotalizerListing::new).toList()
        );
    }

}
