package com.systextil.relatorio.domain.saved_query;

import java.util.List;
import java.util.Map;

import com.systextil.relatorio.domain.ColumnAndTotalizer;
import com.systextil.relatorio.domain.TotalizerTypes;

record OneSavedQueryListing(
	String mainTable,
	String orderBy,
	String pdfTitle,
	byte[] pdfImage,
	List<String> columns,
	List<String> conditions,
	List<String> tablesPairs,
	List<ColumnAndTotalizer> totalizers
) {
	OneSavedQueryListing(SavedQuery savedQuery) {
		this(
			savedQuery.getMainTable(),
			savedQuery.getOrderBy(),
			savedQuery.getPdfTitle(),
			savedQuery.getPdfImage(),
			savedQuery.getSavedQueryColumns()
				.stream()
				.map(c -> c.getColumnName())
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
				.map(t -> new ColumnAndTotalizer(Map.of(t.getTotalizerColumn(), TotalizerTypes.toTotalizerType(t.getTotalizerType()))))
				.toList()
		);
	}
}