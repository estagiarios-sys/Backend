package com.systextil.relatorio.domain.report_data;

import java.sql.SQLException;
import java.util.List;

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
    
	ReportData findDataByFinalQuery(String finalQuery) throws SQLException {
    	connection.connect();
    	ReportData loadedQueryData = repository.findDataByFinalQuery(connection.getIdConnection(), finalQuery);
    	connection.disconnect();
        	
    	return loadedQueryData;
    }
	
	List<String> findDataByTotalizersQuery(String totalizersQuery) throws SQLException {
    	connection.connect();
    	List<String> totalizersResults = repository.findDataByTotalizersQuery(connection.getIdConnection(), totalizersQuery);
    	connection.disconnect();
        	
    	return totalizersResults;
    }
}