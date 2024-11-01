package com.systextil.relatorio.domain.report_data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.MysqlConnection;

@Repository
class MysqlRepository extends ReportDataRepository {
	
    private MysqlConnection connectionMySQL;
    
    ReportData findDataByQuery(String finalQuery, String totalizersQuery) throws SQLException {
    	connectionMySQL = new MysqlConnection();
    	connectionMySQL.connect();
    	ReportData loadedQueryData = super.findDataByQuery(connectionMySQL.getIdConnection(), finalQuery, totalizersQuery);
    	connectionMySQL.disconnect();
        	
    	return loadedQueryData;
    }
    
    int getActualTimeFromQueries(String finalQueryAnalysis, String totalizersQueryAnalysis) throws SQLException {
		connectionMySQL = new MysqlConnection();
		connectionMySQL.connect();
		int actualTimeFromFinalQuery = getActualTimeFromQuery(connectionMySQL.getIdConnection(), finalQueryAnalysis);
    	
    	if (totalizersQueryAnalysis != null) {
    		int actualTimeFromTotalizersQuery = getActualTimeFromQuery(connectionMySQL.getIdConnection(), totalizersQueryAnalysis);
        	connectionMySQL.disconnect();
    		
        	return actualTimeFromFinalQuery + actualTimeFromTotalizersQuery;
    	}
    	connectionMySQL.disconnect();
    	
    	return actualTimeFromFinalQuery;
	}
    
    private int getActualTimeFromQuery(Connection idConnection, String query) throws SQLException {
    	try(PreparedStatement command = idConnection.prepareStatement(query);) {
        	ResultSet data = command.executeQuery();
        	data.next();
        	String allData = data.getString(1);
        	String firstLineData = allData.split("\n", 0)[0];
        	Pattern pattern = Pattern.compile("actual time=(\\d+\\.\\d+)\\.\\.(\\d+\\.\\d+)");
        	Matcher matcher = pattern.matcher(firstLineData);
        	int startTime = 0;
        	int endTime = 0;
        	
        	if (matcher.find()) {
        		startTime = (int) Double.parseDouble(matcher.group(1));
            	endTime = (int) Double.parseDouble(matcher.group(2));
        	}
        	
        	return (startTime + endTime) / 2;
    	} catch (SQLException exception) {
    		throw new SQLException();
    	}
    }
}