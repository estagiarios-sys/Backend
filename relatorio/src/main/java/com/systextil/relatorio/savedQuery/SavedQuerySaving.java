package com.systextil.relatorio.savedQuery;

import jakarta.validation.constraints.NotBlank;

record SavedQuerySaving(
	@NotBlank
	String queryName,
	@NotBlank
	String query
) {}
