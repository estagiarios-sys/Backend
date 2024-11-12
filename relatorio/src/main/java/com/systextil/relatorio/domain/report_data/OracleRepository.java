package com.systextil.relatorio.domain.report_data;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

@Repository
class OracleRepository extends ReportDataRepository {
	
	ReportData findDataByQuery(String finalQuery, String totalizersQuery) throws SQLException {
    	OracleConnection connectionOracle = new OracleConnection();
    	connectionOracle.connect();
    	ReportData loadedQueryData = super.findDataByQuery(connectionOracle.getIdConnection(), finalQuery, totalizersQuery);
    	connectionOracle.disconnect();
        	
    	return loadedQueryData;
    }
}