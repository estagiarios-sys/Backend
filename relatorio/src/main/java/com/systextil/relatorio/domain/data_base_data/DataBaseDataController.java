package com.systextil.relatorio.domain.data_base_data;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("find")
public class DataBaseDataController {

    private DataBaseDataRepository dataBaseDataRepository;
    private FileWriter fileWriter;
    private ObjectMapper objectMapper;
    
    @Value("${tables.json.file.path}")
	private String tablesJsonFilePath;
    
    @Value("${relationships.json.file.path}")
    private String relationshipsJsonFilePath;

    // 0 para oracle e 1 para MySQL
    private static final int ORACLE_MYSQL = 0;
    private static final String FILE_NOT_FOUND_MESSAGE = "Arquivo não encontrado ou não legível: ";
    
    @PostMapping
    public Object[] getQueryReturn(@RequestBody @Valid QueryData queryData) throws SQLException {
        String finalQuery = SqlGenerator.generateFinalQuery(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        String totalizersQuery = null;
        
        if (!queryData.totalizers().isEmpty()) {
        	totalizersQuery = SqlGenerator.generateTotalizersQuery(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        }
        ToLoadQueryData toLoadQueryData = new ToLoadQueryData(finalQuery, totalizersQuery, queryData.totalizers());
        TreatedLoadedQueryData treatedLoadedQueryData = loadQuery(toLoadQueryData);
        ArrayList<String> columnsNameOrNickName = treatedLoadedQueryData.columnsNameOrNickName();
        ArrayList<Object[]> foundObjects = treatedLoadedQueryData.foundObjects();
        Map<String, String> columnsAndTotalizersResult = treatedLoadedQueryData.columnsAndTotalizersResult();
        
        return new Object[]{finalQuery, totalizersQuery, columnsNameOrNickName, foundObjects, columnsAndTotalizersResult};
    }
    
    @PostMapping("analysis")
    public double getQueryAnalysis(@RequestBody @Valid QueryData queryData) throws SQLException {
    	dataBaseDataRepository = new DataBaseDataRepository();
    	int actualTime = 0;
    	
    	if (ORACLE_MYSQL == 1) {
    		String finalQueryAnalysis = SqlGenerator.generateFinalQueryAnalysisFromMySQLDataBase(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        	String totalizersQueryAnalysis = null;
        	
        	if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromMySQLDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        	}
        	actualTime = dataBaseDataRepository.getActualTimeFromQueriesAnalysisFromMySQLDataBase(finalQueryAnalysis, totalizersQueryAnalysis);
    	} else {
    		String[] finalQueryAnaysis = SqlGenerator.generateFinalQueryAnalysisFromOracleDataBase(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
    		String[] totalizersQueryAnalysis = null;
    		
    		if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromOracleDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        	}
    		actualTime = dataBaseDataRepository.getActualTimeFromQueriesAnalysisFromOracleDataBase(finalQueryAnaysis, totalizersQueryAnalysis);
    	}
    	
    	return actualTime;
    }

    @GetMapping("table")
    public ResponseEntity<Resource> getTablesAndColumns() throws IOException {
    	Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resource);
        } else {
            throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }

    @GetMapping("relationship")
    public ResponseEntity<Resource> getRelationships() throws IOException {
        Path filePath = Paths.get(relationshipsJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resource);
        } else {
            throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }

    @PostMapping("loadedQuery")
    public TreatedLoadedQueryData loadQuery(@RequestBody ToLoadQueryData toLoadQueryData) throws SQLException {
        dataBaseDataRepository = new DataBaseDataRepository();
        LoadedQueryData loadedQueryData = null;
        
        if (ORACLE_MYSQL == 1) {
        	loadedQueryData = dataBaseDataRepository.findDataByQueryFromMySQLDataBase(toLoadQueryData.finalQuery(), toLoadQueryData.totalizersQuery());
        } else {
        	loadedQueryData = dataBaseDataRepository.findDataByQueryFromOracleDataBase(toLoadQueryData.finalQuery(), toLoadQueryData.totalizersQuery());
        }

        return treatLoadedQueryData(loadedQueryData, toLoadQueryData.totalizers());
    }

    @PutMapping("update/table")
    public void setTablesAndColumnsFromDatabaseIntoJson() throws IOException, SQLException {
        Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	dataBaseDataRepository = new DataBaseDataRepository();
        	objectMapper = new ObjectMapper();
        	fileWriter = new FileWriter(resource.getFile());
          
        	Map<String, Map<String, String>> tablesAndColumns = null;
        	
            if (ORACLE_MYSQL == 1) {
            	tablesAndColumns = dataBaseDataRepository.getTablesAndColumnsFromMySQLDatabase();
            } else {
            	tablesAndColumns = dataBaseDataRepository.getTablesAndColumnsFromOracleDataBase();
            }
            String json = objectMapper.writeValueAsString(tablesAndColumns);
        	fileWriter.write(json);
        	fileWriter.close();
        } else {
        	throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }

    @PutMapping("update/relationship")
    public void setRelationshipsFromDatabaseIntoJson() throws SQLException, IOException {
        Path filePath = Paths.get(relationshipsJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	dataBaseDataRepository = new DataBaseDataRepository();
        	objectMapper = new ObjectMapper();
        	fileWriter = new FileWriter(resource.getFile());
        	ArrayList<RelationshipData> relationships = null;
        	
        	if (ORACLE_MYSQL == 1) {
        		relationships = dataBaseDataRepository.getRelationshipsFromMySQLDataBase();
        	} else {
        		relationships = dataBaseDataRepository.getRelationshipsFromOracleDataBase();
        	}
        	String json = objectMapper.writeValueAsString(relationships);
        	fileWriter.write(json);
        	fileWriter.close();
        } else {
        	throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }
    
    private TreatedLoadedQueryData treatLoadedQueryData(LoadedQueryData loadedQueryData, Map<String, Totalizer> totalizers) {
    	ArrayList<String> columnsNameOrNickName = columnsNameAndNickNameToColumnsNameOrNickName(loadedQueryData.columnsNameAndNickName());
    	Map<String, String> columnsAndTotalizersResult = null;
    	
    	if (totalizers != null) {
    		columnsAndTotalizersResult = joinColumnsAndTotalizersResult(loadedQueryData, totalizers);
    	}
    	
    	return new TreatedLoadedQueryData(columnsNameOrNickName, loadedQueryData.foundObjects(), columnsAndTotalizersResult);
    }
    
    private Map<String, String> joinColumnsAndTotalizersResult(LoadedQueryData loadedQueryData, Map<String, Totalizer> totalizers) {
    	int totalizersResultsCounter = 0;
        Map<String, String> columnsAndTotalizersResult = new HashMap<>();
        
        for (Map.Entry<String, Totalizer> totalizer : totalizers.entrySet()) {
        	String columnsAndTotalizersColumn = null;
        	
        	for (Map.Entry<String, String> columnNameAndNickName : loadedQueryData.columnsNameAndNickName().entrySet()) {
        		
        		if (totalizer.getKey().equalsIgnoreCase(columnNameAndNickName.getKey())) {
        			if (columnNameAndNickName.getValue() != null) {
        				columnsAndTotalizersColumn = columnNameAndNickName.getValue();
        			} else {
        				columnsAndTotalizersColumn = columnNameAndNickName.getKey();
        			}
        		}
        	}
        	columnsAndTotalizersResult.put(columnsAndTotalizersColumn, totalizer.getValue().toPortuguese() + ": " + loadedQueryData.totalizersResult().get(totalizersResultsCounter));
        	totalizersResultsCounter++;
        }
        
        return columnsAndTotalizersResult;
    }
    
    private ArrayList<String> columnsNameAndNickNameToColumnsNameOrNickName(Map<String, String> columnsNameAndNickName) {
    	ArrayList<String> columnsNameOrNickName = new ArrayList<>();
    	
    	for (Map.Entry<String, String> columnNameAndNickName : columnsNameAndNickName.entrySet()) {
        	
   			if (columnNameAndNickName.getValue() != null) {
   				columnsNameOrNickName.add(columnNameAndNickName.getValue());
   			} else {
   				columnsNameOrNickName.add(columnNameAndNickName.getKey());
    		}
    	}
    	    	
    	return columnsNameOrNickName;
    }
}