package com.systextil.relatorio.record;

import java.util.ArrayList;

public record QueryData(
    String table,
    ArrayList<String> columns,
    ArrayList<String> conditions,
    String orderBy,
    ArrayList<String> joins
) {
}
