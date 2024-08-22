package com.systextil.relatorio.entity;

import java.util.ArrayList;

public record TableData(
    String name,
    ArrayList<String> columns,
    ArrayList<String> conditions,
    String orderBy,
    ArrayList<String> joins
) {
}
