package com.systextil.relatorio.domain.table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

@Repository
class TableOracleRepository {
	
	private final OracleConnection connection;
	private final TableRepository repository;

	public TableOracleRepository(OracleConnection connection, TableRepository repository) {
		this.connection = connection;
		this.repository = repository;
	}
	
	List<String> getTables() throws SQLException {
        connection.connect();
		List<String> tables = repository.getTables(connection.getIdConnection(), "COMERCIAL");
        connection.disconnect();
        
        return tables;
    }
	
	Map<String, String> getColumnsFromTables(String table) throws SQLException {
		connection.connect();
		Map<String, String> columns = repository.getColumnsFromTable(connection.getIdConnection(), table);
		connection.disconnect();
		
		return columns;
	}
}
