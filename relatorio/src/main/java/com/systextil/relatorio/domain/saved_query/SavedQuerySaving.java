package com.systextil.relatorio.domain.saved_query;

import java.util.List;

import com.systextil.relatorio.domain.ColumnAndTotalizer;

record SavedQuerySaving(
	String queryName,
	String table,
	String orderBy,
	String pdfTitle,
	List<SavedQueryColumnSavingListingAndUpdating> columns,
	List<String> conditions,
	List<String> tablesPairs,
	List<ColumnAndTotalizer> totalizers
) {}