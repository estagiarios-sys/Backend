package com.systextil.relatorio.domain.table;

import java.sql.SQLException;
import java.util.Map;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

class OracleRepository extends TableRepository {

	Map<String, Map<String, String>> getTablesFromDataBase() throws SQLException {
        OracleConnection connectionOracle = new OracleConnection();
        connectionOracle.connect();
		Map<String, Map<String, String>> tablesAndColumns = super.getTablesFromDataBase(connectionOracle.getIdConnection(), "ACADEMY", "%");
        connectionOracle.disconnect();
        
        return tablesAndColumns;
    }
}
