package com.systextil.relatorio.repositories;

import com.systextil.relatorio.infra.ConexaoMySql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RepositoryImpl {

    private ConexaoMySql conexao;

    public ArrayList<Object[]> findObjectsByQuery(String sql) throws SQLException, ClassNotFoundException {

        conexao = new ConexaoMySql();
        conexao.conectar();

        PreparedStatement comando = conexao.getIdConexao().prepareStatement(sql);
        ResultSet dados = comando.executeQuery();
        ArrayList<Object[]> listObjects = new ArrayList<>();
        int columnsNumber = dados.getMetaData().getColumnCount();

        while (dados.next()) {
            Object[] object = new Object[columnsNumber];
            for (int i = 1; i <= columnsNumber; i++) {
                object[i - 1] = dados.getString(i);
            }
            listObjects.add(object);
        }

        conexao.desconectar();
        return listObjects;
    }

    public Map<String, String[]> getTablesAndColumns() throws Exception {
        conexao = new ConexaoMySql();
        conexao.conectar();

        Connection connection = conexao.getIdConexao();
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
        conexao = new ConexaoMySql();
        conexao.conectar();

        String sql = "SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE REFERENCED_TABLE_NAME IS NOT NULL";

        PreparedStatement comando = conexao.getIdConexao().prepareStatement(sql);
        ResultSet dados = comando.executeQuery();

        ArrayList<Object> listObjects = new ArrayList<>();

        Map<String, String[]> relationships = new HashMap<>();
        while (dados.next()) {
            String tableName = dados.getString("TABLE_NAME");
            String columnName = dados.getString("COLUMN_NAME");
            String constraintName = dados.getString("CONSTRAINT_NAME");
            String referencedTableName = dados.getString("REFERENCED_TABLE_NAME");
            String referencedColumnName = dados.getString("REFERENCED_COLUMN_NAME");

            String tableAndReferencedTable = tableName + " e " + referencedTableName;
            String join = "INNER JOIN " + referencedTableName + " ON " + tableName + "." + columnName + " = " + referencedTableName + "." + referencedColumnName;
            Object[] relationship = {tableAndReferencedTable, join};
            listObjects.add(relationship);
        }

        conexao.desconectar();
        return listObjects;
    }
}