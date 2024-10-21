package com.systextil.relatorio.domain.table;

import java.sql.SQLException;
import java.util.Map;

import com.systextil.relatorio.infra.data_base_connection.MysqlConnection;

class MysqlRepository extends TableRepository {
	
	Map<String, Map<String, String>> getTablesFromDataBase() throws SQLException {
    	MysqlConnection connectionMySQL = new MysqlConnection();
    	connectionMySQL.connect();
    	Map<String, Map<String, String>> tablesAndColumns = super.getTablesFromDataBase(connectionMySQL.getIdConnection(), "db_gerador_relatorio", "%");
    	connectionMySQL.disconnect();
    	
    	return tablesAndColumns;
    }
}