package com.systextil.relatorio.domain.table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.MysqlConnection;

@Repository
class TableMysqlRepository {
	
	private final MysqlConnection connection;
	private final TableRepository repository;
	
	TableMysqlRepository(MysqlConnection connection, TableRepository repository) {
		this.connection = connection;
		this.repository = repository;
	}
	
	List<String> getTables() throws SQLException {
    	connection.connect();
    	List<String> tables = repository.getTables(connection.getIdConnection(), "db_gerador_relatorio", "%");
    	connection.disconnect();
    	
    	return tables;
    }
	
	Map<String, Map<String, String>> getColumnsFromTable(String table) {
		throw new UnsupportedOperationException("Método não implementado ainda");
	}
}