package com.systextil.relatorio.domain.data_base_data;

import java.util.Map;

public record ColumnAndTotalizer(
	Map<String, Totalizer> totalizers
) {}