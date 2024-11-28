package com.systextil.relatorio.domain.report_data;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.MysqlConnection;

@Repository
class ReportDataMysqlRepository {
	
	private final MysqlConnection connection;
	private final ReportDataRepository repository;
	
	ReportDataMysqlRepository(MysqlConnection connection, ReportDataRepository repository) {
		this.connection = connection;
		this.repository = repository;
	}
    
    ReportData findDataByQuery(String finalQuery, String totalizersQuery) throws SQLException {
    	connection.connect();
    	ReportData loadedQueryData = repository.findDataByQuery(connection.getIdConnection(), finalQuery, totalizersQuery);
    	connection.disconnect();
        	
    	return loadedQueryData;
    }
}