package com.systextil.relatorio.domain.data_base_data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

import com.systextil.relatorio.domain.TotalizerTypes;

/** Record com os dados necessários para a geração do SQL */
record QueryData(
    @NotBlank
    String table,
    @NotEmpty
    List<String> columns,
    String conditions,
    String orderBy,
    List<String> joins,
    Map<String, TotalizerTypes> totalizers
) {}