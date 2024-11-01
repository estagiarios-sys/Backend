package com.systextil.relatorio.domain.report_data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.domain.Totalizer;
import com.systextil.relatorio.infra.exception_handler.CannotConnectToDataBaseException;

import jakarta.validation.Valid;

@Service
class ReportDataService {
	
	private final OracleRepository oracleRepository;
    private final MysqlRepository mySqlRepository;

    @Value("${relationships_with_joins.json.file.path}")
    private String relationshipsWithJoinsJsonFilePath;
    
    @Value("${database.type}")
    private String dataBaseType;
    
    private static final String NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE = "Tipo do banco de dados não configurado";
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
    
    ReportDataService(OracleRepository oracleRepository, MysqlRepository mySqlRepository) {
    	this.oracleRepository = oracleRepository;
    	this.mySqlRepository = mySqlRepository;
    }

    Object[] getQueryReturn(QueryData queryData) throws ParseException, IOException, SQLException {
    	String finalQuery = SqlGenerator.generateFinalQuery(queryData.table(), joinColumnsNameAndNickName(queryData.columns()), queryData.conditions(), queryData.orderBy(), findJoinsByTablesPairs(queryData.tablesPairs()));
        
        if (dataBaseType.equals(ORACLE)) {
        	finalQuery = SqlWithDateConverter.toSqlWithDdMMMyyyy(finalQuery);
        }
        String totalizersQuery = null;
        
        if (!queryData.totalizers().isEmpty()) {
        	totalizersQuery = SqlGenerator.generateTotalizersQuery(queryData.totalizers(), queryData.table(), queryData.conditions(), findJoinsByTablesPairs(queryData.tablesPairs()));
        }
        ReportData loadedQueryData = null;
        
        if (dataBaseType.equals(MYSQL)) {
        	loadedQueryData = mySqlRepository.findDataByQuery(finalQuery, totalizersQuery);
        } else if (dataBaseType.equals(ORACLE)) {
        	loadedQueryData = oracleRepository.findDataByQuery(finalQuery, totalizersQuery);
        } else {
    		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
        }
        
        TreatedReportData treatedLoadedQueryData = treatReportData(loadedQueryData, queryData.totalizers());
        List<String> columnsNameOrNickName = treatedLoadedQueryData.columnsNameOrNickName();
        List<Object[]> foundObjects = treatedLoadedQueryData.foundObjects();
        Map<String, String> columnsAndTotalizersResult = treatedLoadedQueryData.columnsAndTotalizersResult();
        
        return new Object[]{finalQuery, totalizersQuery, columnsNameOrNickName, foundObjects, columnsAndTotalizersResult};
    }
    
    double getQueryAnalysis(@RequestBody @Valid QueryData queryData) throws SQLException, IOException {
    	int actualTime = 0;
    	
    	if (dataBaseType.equals(MYSQL)) {
    		String finalQueryAnalysis = MysqlSqlGenerator.generateFinalQueryAnalysis(queryData.table(), joinColumnsNameAndNickName(queryData.columns()), queryData.conditions(), queryData.orderBy(), findJoinsByTablesPairs(queryData.tablesPairs()));
        	String totalizersQueryAnalysis = null;
        	
        	if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = MysqlSqlGenerator.generateTotalizersQueryAnalysis(queryData.totalizers(), queryData.table(), queryData.conditions(), findJoinsByTablesPairs(queryData.tablesPairs()));
        	}
        	actualTime = mySqlRepository.getActualTimeFromQueries(finalQueryAnalysis, totalizersQueryAnalysis);
    	} else if (dataBaseType.equals(ORACLE)) {
    		String[] finalQueryAnaysis = OracleSqlGenerator.generateFinalQueryAnalysis(queryData.table(), joinColumnsNameAndNickName(queryData.columns()), queryData.conditions(), queryData.orderBy(), findJoinsByTablesPairs(queryData.tablesPairs()));
    		String[] totalizersQueryAnalysis = null;
    		
    		if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = OracleSqlGenerator.generateTotalizersQueryAnalysis(queryData.totalizers(), queryData.table(), queryData.conditions(), findJoinsByTablesPairs(queryData.tablesPairs()));
        	}
    		actualTime = oracleRepository.getActualTimeFromQueries(finalQueryAnaysis, totalizersQueryAnalysis);
    	} else {
    		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
    	}
    	return actualTime;
    }
    
	private TreatedReportData treatReportData(ReportData reportData, Map<String, Totalizer> totalizers) {
    	List<String> columnsNameOrNickName = toColumnsNameOrNickName(reportData.columnsNameAndNickName());
    	Map<String, String> columnsAndTotalizersResult = null;
    	
    	if (totalizers != null) {
    		columnsAndTotalizersResult = joinColumnsAndTotalizersResult(reportData, totalizers);
    	}
    	return new TreatedReportData(columnsNameOrNickName, reportData.foundObjects(), columnsAndTotalizersResult);
    }
    
    private Map<String, String> joinColumnsAndTotalizersResult(ReportData reportData, Map<String, Totalizer> totalizers) {
    	int totalizersResultsCounter = 0;
        Map<String, String> columnsAndTotalizersResult = new HashMap<>();
        
        for (Map.Entry<String, Totalizer> totalizer : totalizers.entrySet()) {
        	String columnsAndTotalizersColumn = null;
        	
        	for (Map.Entry<String, String> columnNameAndNickName : reportData.columnsNameAndNickName().entrySet()) {
        		if (totalizer.getKey().equalsIgnoreCase(columnNameAndNickName.getKey())) {
        			if (columnNameAndNickName.getValue() != null && !columnNameAndNickName.getValue().isBlank()) {
        				columnsAndTotalizersColumn = columnNameAndNickName.getValue();
        			} else {
        				columnsAndTotalizersColumn = columnNameAndNickName.getKey();
        			}
        		}
        	}
        	columnsAndTotalizersResult.put(columnsAndTotalizersColumn, totalizer.getValue().toPortuguese() + ": " + reportData.totalizersResult().get(totalizersResultsCounter));
        	totalizersResultsCounter++;
        }
        return columnsAndTotalizersResult;
    }
    
    private List<String> toColumnsNameOrNickName(Map<String, String> columnsNameAndNickName) {
    	List<String> columnsNameOrNickName = new ArrayList<>();
    	
    	for (Map.Entry<String, String> columnNameAndNickName : columnsNameAndNickName.entrySet()) {
   			if (columnNameAndNickName.getValue() != null) {
   				columnsNameOrNickName.add(columnNameAndNickName.getValue());
   			} else {
   				columnsNameOrNickName.add(columnNameAndNickName.getKey());
    		}
    	}
    	return columnsNameOrNickName;
    }
    
    private List<String> findJoinsByTablesPairs(List<String> tablesPairs) throws IOException {
    	ObjectMapper objectMapper = new ObjectMapper();
    	Path fileOfJoinsPath = Paths.get(relationshipsWithJoinsJsonFilePath);
    	String json = Files.readString(fileOfJoinsPath);
    	List<RelationshipData> relationshipData = objectMapper.readValue(
    			json, objectMapper
    			.getTypeFactory()
    			.constructCollectionType(List.class, RelationshipData.class)
    	);
    	List<String> joins = new ArrayList<>();
    	
    	for (String tablesPair : tablesPairs) {
    		for (RelationshipData tablesPairAndJoin : relationshipData) {
    			if (tablesPair.equals(tablesPairAndJoin.tablesPair())) {
    				joins.add(tablesPairAndJoin.join());
    			}
    		}
    	}
    	
    	return joins;
    }
    
    private List<String> joinColumnsNameAndNickName(List<QueryDataColumn> columns) {
    	List<String> joinedColumnsNameAndNickName = new ArrayList<>();
    	
    	for (QueryDataColumn column : columns) {
    		if (column.nickName() == null || column.nickName().isBlank()) {
    			joinedColumnsNameAndNickName.add(column.name());
    		} else {
    			joinedColumnsNameAndNickName.add(column.name() + " AS \"" + column.nickName() + "\"");
    		}
    	}
    	
    	return joinedColumnsNameAndNickName;
    }
}