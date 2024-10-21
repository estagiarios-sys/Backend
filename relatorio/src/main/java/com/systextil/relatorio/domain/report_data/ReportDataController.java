package com.systextil.relatorio.domain.report_data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.infra.exception_handler.CannotConnectToDataBaseException;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("report-data")
public class ReportDataController {

    private final OracleRepository oracleRepository;
    private final MysqlRepository mySqlRepository;

    @Value("${relationships_with_joins.json.file.path}")
    private String relationshipsWithJoinsJsonFilePath;
    
    @Value("${database.type}")
    private String dataBaseType;
    
    private static final String NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE = "Tipo do banco de dados não configurado";
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
    
    public ReportDataController(OracleRepository oracleDataBaseRepository, MysqlRepository mySqlDataBaseRepository) {
    	this.oracleRepository = oracleDataBaseRepository;
    	this.mySqlRepository = mySqlDataBaseRepository;
    }
    
    @PostMapping
    public Object[] getQueryReturn(@RequestBody @Valid QueryData queryData) throws SQLException, ParseException, IOException {
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
        
        TreatedReportData treatedLoadedQueryData = ReportDataTreater.treatLoadedQueryData(loadedQueryData, queryData.totalizers());
        ArrayList<String> columnsNameOrNickName = treatedLoadedQueryData.columnsNameOrNickName();
        ArrayList<Object[]> foundObjects = treatedLoadedQueryData.foundObjects();
        Map<String, String> columnsAndTotalizersResult = treatedLoadedQueryData.columnsAndTotalizersResult();
        
        return new Object[]{finalQuery, totalizersQuery, columnsNameOrNickName, foundObjects, columnsAndTotalizersResult};
    }
    
    @PostMapping("analyze")
    public double getQueryAnalysis(@RequestBody @Valid QueryData queryData) throws SQLException, IOException {
    	int actualTime = 0;
    	
    	if (dataBaseType.equals(MYSQL)) {
    		String finalQueryAnalysis = SqlGenerator.generateFinalQueryAnalysisFromMySQLDataBase(queryData.table(), joinColumnsNameAndNickName(queryData.columns()), queryData.conditions(), queryData.orderBy(), findJoinsByTablesPairs(queryData.tablesPairs()));
        	String totalizersQueryAnalysis = null;
        	
        	if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromMySQLDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), findJoinsByTablesPairs(queryData.tablesPairs()));
        	}
        	actualTime = mySqlRepository.getActualTimeFromQueries(finalQueryAnalysis, totalizersQueryAnalysis);
    	} else if (dataBaseType.equals(ORACLE)) {
    		String[] finalQueryAnaysis = SqlGenerator.generateFinalQueryAnalysisFromOracleDataBase(queryData.table(), joinColumnsNameAndNickName(queryData.columns()), queryData.conditions(), queryData.orderBy(), findJoinsByTablesPairs(queryData.tablesPairs()));
    		String[] totalizersQueryAnalysis = null;
    		
    		if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromOracleDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), findJoinsByTablesPairs(queryData.tablesPairs()));
        	}
    		actualTime = oracleRepository.getActualTimeFromQueries(finalQueryAnaysis, totalizersQueryAnalysis);
    	} else {
    		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
    	}
    	
    	return actualTime;
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
}