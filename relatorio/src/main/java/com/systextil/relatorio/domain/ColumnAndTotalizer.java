package com.systextil.relatorio.domain;

import java.util.Map;

public record ColumnAndTotalizer(
	Map<String, TotalizerTypes> totalizer
) {}