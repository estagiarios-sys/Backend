package com.systextil.relatorio.domain.saved_query;

import java.util.List;

import com.systextil.relatorio.domain.ColumnAndTotalizer;

record SavedQueryUpdating(
	String mainTable,
	String orderBy,
	String pdfTitle,
	List<SavedQueryColumnSavingListingAndUpdating> columns,
	List<String> conditions,
	List<String> tablesPairs,
	List<ColumnAndTotalizer> totalizers
) {}