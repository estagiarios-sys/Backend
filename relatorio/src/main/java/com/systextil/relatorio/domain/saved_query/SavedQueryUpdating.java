package com.systextil.relatorio.domain.saved_query;

import java.util.List;
import java.util.Map;

import com.systextil.relatorio.domain.TotalizerTypes;

record SavedQueryUpdating(
	String table,
	String orderBy,
	String pdfTitle,
	List<SavedQueryColumnSavingListingAndUpdating> columns,
	List<String> conditions,
	List<String> tablesPairs,
	Map<String, TotalizerTypes> totalizers
) {}