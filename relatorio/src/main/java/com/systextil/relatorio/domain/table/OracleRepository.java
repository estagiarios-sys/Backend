package com.systextil.relatorio.domain.table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

class OracleRepository {
	
	private OracleConnection connection;
	private TableRepository repository;

	List<String> getTables() throws SQLException {
		connection = new OracleConnection();
		repository = new TableRepository();
        connection.connect();
		List<String> tables = repository.getTables(connection.getIdConnection(), "COMERCIAL", "%");
        connection.disconnect();
        
        return tables;
    }
	
	Map<String, Map<String, String>> getColumnsFromTables(AllTables allTables) throws SQLException {
		connection = new OracleConnection();
		repository = new TableRepository();
		connection.connect();
		Map<String, Map<String, String>> tablesAndColumns = repository.getColumnsFromTables(connection.getIdConnection(), allTables);
		connection.disconnect();
		
		return tablesAndColumns;
	}
}
