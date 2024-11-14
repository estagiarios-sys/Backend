package com.systextil.relatorio.domain.table;

import java.sql.SQLException;
import java.util.List;

import com.systextil.relatorio.infra.data_base_connection.MysqlConnection;

class MysqlRepository extends TableRepository {
	
	List<String> getTables() throws SQLException {
    	MysqlConnection connectionMySQL = new MysqlConnection();
    	connectionMySQL.connect();
    	List<String> tables = super.getTables(connectionMySQL.getIdConnection(), "db_gerador_relatorio", "%");
    	connectionMySQL.disconnect();
    	
    	return tables;
    }
}