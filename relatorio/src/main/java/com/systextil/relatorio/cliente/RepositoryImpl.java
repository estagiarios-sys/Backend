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

    public ArrayList<Object[]> findObjectByColumns(String colunas, String tabela) throws SQLException, ClassNotFoundException {
        conexao = new ConexaoMySql();
        conexao.conectar();

        String sql = "SELECT " + colunas + " FROM " + tabela;
        PreparedStatement comando = conexao.getIdConexao().prepareStatement(sql);
        ResultSet dados = comando.executeQuery();
        ArrayList<Object[]> listaClientes = new ArrayList<>();
        int columnsNumber = dados.getMetaData().getColumnCount();

        while(dados.next()) {
            Object[] cliente = new Object[columnsNumber];
            for (int i = 1; i <= columnsNumber; i++) {
                 cliente[i - 1] = dados.getString(i);
            }
            listaClientes.add(cliente);
        }

        conexao.desconectar();
        return listaClientes;
    }

    public Map<String, String[]> getTablesAndColumns() throws Exception {
        conexao = new ConexaoMySql();
        conexao.conectar();

        Connection connection = conexao.getIdConexao();
        Map<String, String[]> tablesAndColumns = new HashMap<>();

        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");

            ResultSet columns = metaData.getColumns(null, null, tableName, "%");
            String[] columnNames = new String[columns.getMetaData().getColumnCount()];
            int i = 0;
            while (columns.next()) {
                columnNames[i++] = columns.getString("COLUMN_NAME");
            }
            tablesAndColumns.put(tableName, columnNames);
        }

        return tablesAndColumns;
    }
}
