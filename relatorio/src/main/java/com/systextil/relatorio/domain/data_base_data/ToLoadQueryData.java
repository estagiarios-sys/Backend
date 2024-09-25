package com.systextil.relatorio.domain.data_base_data;

import java.util.List;

/** Record usado no DataBaseDataController.loadQuery() para retornar um LoadedQueryData */
public record ToLoadQueryData(
		String finalQuery,
		String totalizersQuery,
		List<ColumnAndTotalizer> totalizers
) {}