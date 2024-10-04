package com.systextil.relatorio.domain.saved_query;

import java.util.List;

record SavedQueryListing(
        String queryName,
        String finalQuery,
        String totalizersQuery,
        byte[] imgPDF,
        String titlePDF,
        List<TotalizerListing> totalizers
) {
    SavedQueryListing(SavedQuery savedQuery) {
        this(
                savedQuery.getQueryName(),
                savedQuery.getFinalQuery(),
                savedQuery.getTotalizersQuery(),
                savedQuery.getImagePDF(),
                savedQuery.getTitlePDF(),
                savedQuery.getTotalizers().stream().map(TotalizerListing::new).toList()
        );
    }
}