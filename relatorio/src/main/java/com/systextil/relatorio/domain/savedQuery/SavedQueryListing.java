package com.systextil.relatorio.domain.savedQuery;

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
                savedQuery.getImgPDF(),
                savedQuery.getTitlePDF(),
                savedQuery.getTotalizers().stream().map(TotalizerListing::new).toList()
        );
    }

}