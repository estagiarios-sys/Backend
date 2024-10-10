package com.systextil.relatorio.domain.saved_query;

import java.util.List;

import com.systextil.relatorio.domain.ColumnAndTotalizer;

record SavedQueryUpdating(
	String mainTable,
	String conditions,
	String orderBy,
	String pdfTitle,
	List<String> columns,
	List<String> joins,
	List<ColumnAndTotalizer> totalizers
) {}