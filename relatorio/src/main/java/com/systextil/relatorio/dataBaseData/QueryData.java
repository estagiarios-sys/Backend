package com.systextil.relatorio.dataBaseData;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;

record QueryData(
    @NotBlank
    String table,
    @NotEmpty
    ArrayList<String> columns,
    String conditions,
    String orderBy,
    ArrayList<String> joins
) {}
