package com.systextil.relatorio.domain.report_data;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.MysqlConnection;

@Repository
class MysqlRepository extends ReportDataRepository {
    
    ReportData findDataByQuery(String finalQuery, String totalizersQuery) throws SQLException {
    	MysqlConnection connectionMySQL = new MysqlConnection();
    	connectionMySQL.connect();
    	ReportData loadedQueryData = super.findDataByQuery(connectionMySQL.getIdConnection(), finalQuery, totalizersQuery);
    	connectionMySQL.disconnect();
        	
    	return loadedQueryData;
    }
}