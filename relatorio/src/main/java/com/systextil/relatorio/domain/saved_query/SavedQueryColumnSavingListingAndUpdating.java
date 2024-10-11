package com.systextil.relatorio.domain.saved_query;

record SavedQueryColumnSavingListingAndUpdating(
	String name,
	String nickName,
	String type
) {
	SavedQueryColumnSavingListingAndUpdating(SavedQueryColumn column) {
		this(
			column.getColumnName(),
			column.getColumnNickName(),
			column.getColumnType()
		);
	}
}