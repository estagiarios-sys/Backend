package com.systextil.relatorio.domain.dataBaseData;

/** Record usado no DataBaseDataController.loadQuery() para retornar um LoadedQueryData */
record ToLoadQueryData(
		String finalQuery,
		QueryWithTotalizers queryWithTotalizers
		) {}
