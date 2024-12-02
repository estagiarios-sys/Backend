package com.systextil.relatorio.domain.report_data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

@Repository
class ReportDataOracleRepository {
	
	private final OracleConnection connection;
	private final ReportDataRepository repository;
	
	ReportDataOracleRepository(OracleConnection connection, ReportDataRepository repository) {
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
	
	int getActualTimeFromQuery(String[] query) throws SQLException {
		connection.connect();
		Connection idConnection = connection.getIdConnection();
		int actualTime = 0;
		
		try(PreparedStatement explainPlanForCommand = idConnection.prepareStatement(query[0]);
			PreparedStatement planDisplayCommand = idConnection.prepareStatement(query[1])) {
	    	explainPlanForCommand.execute();
	    	ResultSet data = planDisplayCommand.executeQuery();
	    	data.next();
	    	actualTime = data.getInt(1);
		} catch (SQLException exception) {
			throw new SQLException(exception.getLocalizedMessage());
		} finally {
			connection.disconnect();
		}
    	return actualTime;
    }
}