package com.systextil.relatorio.domain.report_data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

@Repository
public class OracleRepository extends ReportDataRepository {

    private OracleConnection connectionOracle;
	
	ReportData findDataByQuery(String finalQuery, String totalizersQuery) throws SQLException {
    	connectionOracle = new OracleConnection();
    	connectionOracle.connect();
    	ReportData loadedQueryData = super.findDataByQuery(connectionOracle.getIdConnection(), finalQuery, totalizersQuery);
    	connectionOracle.disconnect();
        	
    	return loadedQueryData;
    }
	
	int getActualTimeFromQueries(String[] finalQueryAnalysis, String[] totalizersQueryAnalysis) throws SQLException {
		connectionOracle = new OracleConnection();
		connectionOracle.connect();
		int actualTimeFromFinalQuery = getActualTimeFromQuery(connectionOracle.getIdConnection(), finalQueryAnalysis);
		
		if (totalizersQueryAnalysis != null) {
			int actualTimeFromTotalizersQuery = getActualTimeFromQuery(connectionOracle.getIdConnection(), totalizersQueryAnalysis);
			connectionOracle.disconnect();
			
			return actualTimeFromFinalQuery + actualTimeFromTotalizersQuery;
		}
		connectionOracle.disconnect();
		
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