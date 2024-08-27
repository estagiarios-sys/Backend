package com.systextil.relatorio.record;

import java.util.ArrayList;

public record QueryData(
    String name,
    ArrayList<String> columns,
    ArrayList<String> conditions,
    String orderBy,
    ArrayList<String> joins
) {
}
