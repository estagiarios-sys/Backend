package com.systextil.relatorio.domain.dataBaseData;

import java.util.Map;

/** Record usado no DataBaseDataController.loadQuery() para retornar um LoadedQueryData */
record ToLoadQueryData(
		String finalQuery,
		String totalizersQuery,
		Map<String, Totalizer> totalizers
) {}