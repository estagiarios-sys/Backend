package com.systextil.relatorio.domain.data_base_data;

import java.util.List;

import com.systextil.relatorio.domain.ColumnAndTotalizer;

/** Record usado no DataBaseDataController.loadQuery() para retornar um LoadedQueryData */
record ToLoadQueryData(
		String finalQuery,
		String totalizersQuery,
		List<ColumnAndTotalizer> totalizers
) {}