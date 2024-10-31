package com.systextil.relatorio.domain.report_data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

import com.systextil.relatorio.domain.Totalizer;

/** Record com os dados necessários para a geração do SQL */
record QueryData(
    @NotBlank
    String table,
    @NotEmpty
    List<QueryDataColumn> columns,
    @NotNull
    List<String> conditions,
    @NotNull
    String orderBy,
    @NotNull
    List<String> tablesPairs,
    @NotNull
    Map<String, Totalizer> totalizers
) {}