package com.systextil.relatorio.entity;

import java.util.ArrayList;

public class TableData {

    private String name;
    private ArrayList<String> columns;
    private ArrayList<String> conditions;
    private String orderBy;
    private ArrayList<String> joins;

    public TableData() {
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public ArrayList<String> getConditions() {
        return conditions;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public ArrayList<String> getJoin() {
        return joins;
    }
}
