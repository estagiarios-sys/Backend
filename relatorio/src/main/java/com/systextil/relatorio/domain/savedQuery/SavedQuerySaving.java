package com.systextil.relatorio.domain.savedQuery;

import java.util.ArrayList;

import jakarta.validation.constraints.NotBlank;

record SavedQuerySaving(
	@NotBlank
	String queryName,
	@NotBlank
	String finalQuery,
	String totalizersQuery,
	ArrayList<TotalizerSaving> totalizers
) {}
