package com.systextil.relatorio.domain.saved_query;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.systextil.relatorio.domain.Totalizer;

record OneSavedQueryListing(
	String table,
	String orderBy,
	String pdfTitle,
	byte[] pdfImage,
	List<SavedQueryColumnSavingListingAndUpdating> columns,
	List<String> conditions,
	List<String> tablesPairs,
	Map<String, Totalizer> totalizers
) {
	OneSavedQueryListing(SavedQuery savedQuery) {
		this(
			savedQuery.getMainTable(),
			savedQuery.getOrderBy() != null ? savedQuery.getOrderBy() : "", 
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
				.collect(Collectors.toMap(t -> t.getTotalizerColumn(), t -> Totalizer.toTotalizerType(t.getTotalizerType())))
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		OneSavedQueryListing oneSavedQueryListing = (OneSavedQueryListing) obj;
		return table.equals(oneSavedQueryListing.table())
				&& orderBy.equals(oneSavedQueryListing.orderBy())
				&& pdfTitle.equals(oneSavedQueryListing.pdfTitle())
				&& Arrays.equals(pdfImage, oneSavedQueryListing.pdfImage())
				&& columns.equals(oneSavedQueryListing.columns())
				&& conditions.equals(oneSavedQueryListing.conditions())
				&& tablesPairs.equals(oneSavedQueryListing.tablesPairs())
				&& totalizers.equals(oneSavedQueryListing.totalizers());
	}

	@Override
    public int hashCode() {
        int result = Objects.hash(table, orderBy, pdfTitle, columns, conditions, tablesPairs, totalizers);
        result = 31 * result + Arrays.hashCode(pdfImage);
        return result;
    }
	
	@Override
	public String toString() {
		return "OneSavedQueryListing{" +
				"table=" + table +
				", orderBy=" + orderBy +
				", pdfTitle=" + pdfTitle +
				", pdfImage=" + Arrays.toString(pdfImage) +
				", columns=" + columns +
				", conditions=" + conditions +
				", tablesPairs=" + tablesPairs +
				", totalizers=" + totalizers +
				"}";
	}
}