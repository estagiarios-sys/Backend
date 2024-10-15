package com.systextil.relatorio.domain.saved_query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.systextil.relatorio.domain.TotalizerTypes;

record OneSavedQueryListing(
	String table,
	String orderBy,
	String pdfTitle,
	byte[] pdfImage,
	List<SavedQueryColumnSavingListingAndUpdating> columns,
	List<String> conditions,
	List<String> tablesPairs,
	Map<String, TotalizerTypes> totalizers
) {
	OneSavedQueryListing(SavedQuery savedQuery) {
		this(
			savedQuery.getMainTable(),
			savedQuery.getOrderBy(),
			savedQuery.getPdfTitle(),
			savedQuery.getPdfImage(),
			savedQuery.getSavedQueryColumns()
				.stream()
				.map(SavedQueryColumnSavingListingAndUpdating::new)
				.toList(),
			savedQuery.getSavedQueryConditions()
				.stream()
				.map(c -> c.getConditionName())
				.toList(),
			savedQuery.getSavedQueryJoins()
				.stream()
				.map(j -> j.getJoinName())
				.toList(),
			savedQuery.getSavedQueryTotalizers()
				.stream()
				.collect(Collectors.toMap(t -> t.getTotalizerColumn(), t -> TotalizerTypes.toTotalizerType(t.getTotalizerType())))
		);
	}
}