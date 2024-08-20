package com.systextil.relatorio.cliente;

import com.systextil.relatorio.infra.ConexaoMySql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class RepositoryImpl {

    private ConexaoMySql conexao;

    public ArrayList<Object[]> findObjectByColumns(String sql) throws SQLException, ClassNotFoundException {
        conexao = new ConexaoMySql();
        conexao.conectar();

        PreparedStatement comando = conexao.getIdConexao().prepareStatement(sql);
        ResultSet dados = comando.executeQuery();
        ArrayList<Object[]> listObjects = new ArrayList<>();
        int columnsNumber = dados.getMetaData().getColumnCount();

        while(dados.next()) {
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
}
