package com.systextil.relatorio.repositories;

import com.systextil.relatorio.infra.ConnectionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainRepository {

    private ConnectionMySQL connection;

    public ArrayList<Object[]> findObjectsByQuery(String sql) throws SQLException, ClassNotFoundException {

        connection = new ConnectionMySQL();
        connection.connect();

        PreparedStatement command = connection.getIdConnection().prepareStatement(sql);
        ResultSet data = command.executeQuery();
        ArrayList<Object[]> listObjects = new ArrayList<>();
        int columnsNumber = data.getMetaData().getColumnCount();

        while (data.next()) {
            Object[] object = new Object[columnsNumber];
            for (int i = 1; i <= columnsNumber; i++) {
                object[i - 1] = data.getString(i);
            }
            listObjects.add(object);
        }

        connection.disconnect();
        return listObjects;
    }

    public Map<String, String[]> getTablesAndColumns() throws Exception {
        connection = new ConnectionMySQL();
        connection.connect();

        Connection connection = this.connection.getIdConnection();
        Map<String, String[]> tablesAndColumns = new HashMap<>();

        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet tables = metaData.getTables("db_gerador_relatorio", null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");

            ResultSet columns = metaData.getColumns(null, null, tableName, "%");
            ArrayList<String> columnNames = new ArrayList<>();
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if (columnName != null) {
                    columnNames.add(columnName);
                }
            }
            tablesAndColumns.put(tableName, columnNames.toArray(new String[0]));
        }

        return tablesAndColumns;
    }

    public ArrayList<Object> getRelationship() throws SQLException, ClassNotFoundException {
        connection = new ConnectionMySQL();
        connection.connect();

        String sql = "SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE REFERENCED_TABLE_NAME IS NOT NULL";

        PreparedStatement comando = connection.getIdConnection().prepareStatement(sql);
        ResultSet dados = comando.executeQuery();

        ArrayList<Object> listObjects = new ArrayList<>();

        while (dados.next()) {
            String tableName = dados.getString("TABLE_NAME");
            String columnName = dados.getString("COLUMN_NAME");
            String referencedTableName = dados.getString("REFERENCED_TABLE_NAME");
            String referencedColumnName = dados.getString("REFERENCED_COLUMN_NAME");
            String tableAndReferencedTable = tableName + " e " + referencedTableName;
            String join = "INNER JOIN " + referencedTableName + " ON " + tableName + "." + columnName + " = " + referencedTableName + "." + referencedColumnName;
            Object[] relationship = {tableAndReferencedTable, join};
            listObjects.add(relationship);
        }
        connection.disconnect();
        return listObjects;
    }
}