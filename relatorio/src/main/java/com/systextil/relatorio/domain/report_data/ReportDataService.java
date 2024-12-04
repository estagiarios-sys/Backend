package com.systextil.relatorio.domain.report_data;

import static com.systextil.relatorio.domain.report_data.SqlGenerator.*;
import static com.systextil.relatorio.domain.report_data.SqlWithDateConverter.*;
import static com.systextil.relatorio.domain.report_data.ReportDataProcessor.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.systextil.relatorio.infra.exception_handler.IllegalDataBaseTypeException;
import com.systextil.relatorio.infra.exception_handler.UnsupportedHttpStatusException;

@Service
class ReportDataService {
	
	private final ReportDataOracleRepository oracleRepository;
    private final ReportDataMysqlRepository mySqlRepository;
    private final ReportDataMicroserviceClient microserviceClient;
    private final QueryDataPreparer queryDataPreparer;
    private final String dataBaseType;
    
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
    
    ReportDataService(
    		ReportDataOracleRepository oracleRepository,
    		ReportDataMysqlRepository mySqlRepository,
    		ReportDataMicroserviceClient microserviceClient,
    		QueryDataPreparer queryDataPreparer,
    		@Value("${database.type}") String dataBaseType
    		) {
    	this.oracleRepository = oracleRepository;
    	this.mySqlRepository = mySqlRepository;
    	this.microserviceClient = microserviceClient;
    	this.queryDataPreparer = queryDataPreparer;
    	this.dataBaseType = dataBaseType;
    }

    Object[] getQueryReturn(QueryData queryData) throws ParseException, IOException, SQLException {
    	String finalQuery = generateFinalQuery(
    			queryData.table(),
    			queryDataPreparer.joinColumnsNameAndNickName(queryData.columns()),
    			queryData.conditions(),
    			queryData.orderBy(),
    			queryDataPreparer.findJoinsByTablesPairs(queryData.tablesPairs())
    			);
    	
        if (dataBaseType.equals(ORACLE)) {
        	finalQuery = toSqlWithDdMMyyyy(finalQuery);
        }
        String totalizersQuery = null;
        
        if (!queryData.totalizers().isEmpty()) {
        	totalizersQuery = generateTotalizersQuery(
        			queryData.totalizers(),
        			queryData.table(),
        			queryData.conditions(),
        			queryDataPreparer.findJoinsByTablesPairs(queryData.tablesPairs())
        			);
        }
        ReportData reportData = findDataByQueries(finalQuery, totalizersQuery);
        List<String> columnsNameOrNickName = toColumnsNameOrNickName(reportData.columnsNameAndNickName());
        List<Object[]> foundObjects = reportData.foundObjects();
        Map<String, String> columnsAndTotalizersResult = joinColumnsAndTotalizersResult(reportData, queryData.totalizers());
        
        return new Object[]{finalQuery, totalizersQuery, columnsNameOrNickName, foundObjects, columnsAndTotalizersResult};
    }
    
    int getQueryAnalysis(QueryData queryData) throws IOException, ParseException {
    	String finalQuery = generateFinalQuery(
    			queryData.table(),
    			queryDataPreparer.joinColumnsNameAndNickName(queryData.columns()),
    			queryData.conditions(),
    			queryData.orderBy(),
    			queryDataPreparer.findJoinsByTablesPairs(queryData.tablesPairs())
    			);
    	
    	if (dataBaseType.equals(ORACLE)) {
        	finalQuery = toSqlWithDdMMyyyy(finalQuery);
        }
    	String totalizersQuery = null;
        
        if (!queryData.totalizers().isEmpty()) {
        	totalizersQuery = generateTotalizersQuery(
        			queryData.totalizers(),
        			queryData.table(),
        			queryData.conditions(),
        			queryDataPreparer.findJoinsByTablesPairs(queryData.tablesPairs())
        			);
        }
    	
    	int queryAnalysis = 0;
    	
    	if (dataBaseType.equals(MYSQL)) {
    		throw new UnsupportedOperationException("MySQL ainda não é 100% suportado");
    	} else if (dataBaseType.equals(ORACLE)) {
    		queryAnalysis += getResponseBody(finalQuery);
    		
    		if (totalizersQuery != null) {
        		queryAnalysis += getResponseBody(totalizersQuery);
    		}
    	} else {
    		throw new IllegalDataBaseTypeException(dataBaseType);
    	}
    	return queryAnalysis;
    }

    private ReportData findDataByQueries(String finalQuery, String totalizersQuery) throws SQLException {
    	ReportData reportData = null;
    	
    	if (dataBaseType.equals(MYSQL)) {
        	reportData = mySqlRepository.findDataByFinalQuery(finalQuery);
        	
        	if (totalizersQuery != null) {
        		reportData = reportData.updateData(reportData, mySqlRepository.findDataByTotalizersQuery(totalizersQuery));
        	}
        } else if (dataBaseType.equals(ORACLE)) {
        	reportData = oracleRepository.findDataByFinalQuery(finalQuery);
        	
        	if (totalizersQuery != null) {
        		reportData = reportData.updateData(reportData, oracleRepository.findDataByTotalizersQuery(totalizersQuery));
        	}
        } else {
    		throw new IllegalDataBaseTypeException(dataBaseType);
        }
    	return reportData;
    }
    
    private int getResponseBody(String sql) {
    	ResponseEntity<Integer> response = microserviceClient.getQueryAnalysis(sql);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			if (response.getBody() != null) {
				return response.getBody();
			} else {
				throw new NullPointerException();
			}
		} else {
			throw new UnsupportedHttpStatusException(response.getStatusCode());
		}
    }
}