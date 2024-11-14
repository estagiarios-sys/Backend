package com.systextil.relatorio.domain.table;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record AllTables(
	@NotBlank
	String mainTable,
	@NotNull
	List<String> tablesPairs
) {}