package com.systextil.relatorio.domain.data_base_data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.Map;

/** Record com os dados necessários para a geração do SQL */
record QueryData(
    @NotBlank
    String table,
    @NotEmpty
    ArrayList<String> columns,
    String conditions,
    String orderBy,
    ArrayList<String> joins,
    Map<String, Totalizer> totalizers
) {}