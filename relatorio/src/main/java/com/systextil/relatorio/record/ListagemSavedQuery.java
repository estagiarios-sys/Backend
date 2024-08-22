package com.systextil.relatorio.record;

import com.systextil.relatorio.entity.SavedQuery;

public record ListagemSavedQuery(
        String queryName,
        String query
) {
    public ListagemSavedQuery(SavedQuery savedQuery) {
        this(
                savedQuery.getQueryName(),
                savedQuery.getQuery()
        );
    }

}
