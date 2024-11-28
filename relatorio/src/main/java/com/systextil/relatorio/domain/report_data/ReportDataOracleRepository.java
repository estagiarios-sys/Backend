package com.systextil.relatorio.domain.report_data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	ReportData findDataByQuery(String finalQuery, String totalizersQuery) throws SQLException {
    	connection.connect();
    	ReportData loadedQueryData = repository.findDataByQuery(connection.getIdConnection(), finalQuery, totalizersQuery);
    	connection.disconnect();
        	
    	return loadedQueryData;
    }
	
	int getActualTimeFromQueries(String[] finalQueryAnalysis, String[] totalizersQueryAnalysis) throws SQLException {
		connection.connect();
		int actualTimeFromFinalQuery = getActualTimeFromQuery(connection.getIdConnection(), finalQueryAnalysis);
		
		if (totalizersQueryAnalysis != null) {
			int actualTimeFromTotalizersQuery = getActualTimeFromQuery(connection.getIdConnection(), totalizersQueryAnalysis);
			connection.disconnect();
			
			return actualTimeFromFinalQuery + actualTimeFromTotalizersQuery;
		}
		connection.disconnect();
		
		return actualTimeFromFinalQuery;
	}
	
	private int getActualTimeFromQuery(Connection idConnection, String[] query) throws SQLException {		
		try(PreparedStatement explainPlanForCommand = idConnection.prepareStatement(query[0]);
			PreparedStatement planDisplayCommand = idConnection.prepareStatement(query[1])) {
	    	explainPlanForCommand.execute();
	    	ResultSet data = planDisplayCommand.executeQuery();
	    	data.next();
	    	return data.getInt(1);
		} catch (SQLException exception) {
			throw new SQLException(exception.getLocalizedMessage());
		}
    }
}