package com.systextil.relatorio.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;

public record QueryData(

    @NotBlank
    String table,

    @NotEmpty
    ArrayList<String> columns,
    String conditions,
    String orderBy,
    ArrayList<String> joins
) {}