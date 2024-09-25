package com.systextil.relatorio.domain.data_base_data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.infra.exception_handler.CannotConnectToDataBaseException;

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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RestController
@RequestMapping("find")
public class DataBaseDataController {

    private final OracleDataBaseDataRepository oracleDataBaseRepository;
    private final MySqlDataBaseDataRepository mySqlDataBaseRepository;
    private FileWriter fileWriter;
    private ObjectMapper objectMapper;
    
    @Value("${tables.json.file.path}")
	private String tablesJsonFilePath;
    
    @Value("${relationships.json.file.path}")
    private String relationshipsJsonFilePath;

    @Value("${database.type}")
    private String dataBaseType;
    
    private static final String FILE_NOT_FOUND_MESSAGE = "Arquivo não encontrado ou não legível: ";
    private static final String NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE = "Tipo do banco de dados não configurado";
    
    public DataBaseDataController(OracleDataBaseDataRepository oracleDataBaseRepository, MySqlDataBaseDataRepository mySqlDataBaseRepository) {
    	this.oracleDataBaseRepository = oracleDataBaseRepository;
    	this.mySqlDataBaseRepository = mySqlDataBaseRepository;
    }
    
    @PostMapping
    public Object[] getQueryReturn(@RequestBody @Valid QueryData queryData) throws SQLException {
        String finalQuery = SqlGenerator.generateFinalQuery(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        String totalizersQuery = null;
        
        if (!queryData.totalizers().isEmpty()) {
        	totalizersQuery = SqlGenerator.generateTotalizersQuery(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        }
        List<ColumnAndTotalizer> totalizers = new ArrayList<>();
        
        for (Map.Entry<String, Totalizer> entry : queryData.totalizers().entrySet()) {
        	totalizers.add(new ColumnAndTotalizer(Map.of(entry.getKey(), entry.getValue())));
        }
        ToLoadQueryData toLoadQueryData = new ToLoadQueryData(finalQuery, totalizersQuery, totalizers);
        TreatedLoadedQueryData treatedLoadedQueryData = loadQuery(toLoadQueryData);
        ArrayList<String> columnsNameOrNickName = treatedLoadedQueryData.columnsNameOrNickName();
        ArrayList<Object[]> foundObjects = treatedLoadedQueryData.foundObjects();
        Map<String, String> columnsAndTotalizersResult = treatedLoadedQueryData.columnsAndTotalizersResult();
        
        return new Object[]{finalQuery, totalizersQuery, columnsNameOrNickName, foundObjects, columnsAndTotalizersResult};
    }
    
    @PostMapping("analysis")
    public double getQueryAnalysis(@RequestBody @Valid QueryData queryData) throws SQLException {
    	int actualTime = 0;
    	
    	if (dataBaseType.equals("mysql")) {
    		String finalQueryAnalysis = SqlGenerator.generateFinalQueryAnalysisFromMySQLDataBase(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        	String totalizersQueryAnalysis = null;
        	
        	if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromMySQLDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        	}
        	actualTime = mySqlDataBaseRepository.getActualTimeFromQueriesAnalysisFromDataBase(finalQueryAnalysis, totalizersQueryAnalysis);
    	} else if (dataBaseType.equals("oracle")) {
    		String[] finalQueryAnaysis = SqlGenerator.generateFinalQueryAnalysisFromOracleDataBase(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
    		String[] totalizersQueryAnalysis = null;
    		
    		if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromOracleDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        	}
    		actualTime = oracleDataBaseRepository.getActualTimeFromQueriesAnalysisFromDataBase(finalQueryAnaysis, totalizersQueryAnalysis);
    	} else {
    		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
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
        LoadedQueryData loadedQueryData = null;
        
        if (dataBaseType.equals("mysql")) {
        	loadedQueryData = mySqlDataBaseRepository.findDataByQuery(toLoadQueryData.finalQuery(), toLoadQueryData.totalizersQuery());
        } else if (dataBaseType.equals("oracle")) {
        	loadedQueryData = oracleDataBaseRepository.findDataByQuery(toLoadQueryData.finalQuery(), toLoadQueryData.totalizersQuery());
        } else {
    		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
        }

        return treatLoadedQueryData(loadedQueryData, toLoadQueryData.totalizers());
    }

    @PutMapping("update/table")
    public void setTablesAndColumnsFromDatabaseIntoJson() throws IOException, SQLException {
        Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	objectMapper = new ObjectMapper();
        	fileWriter = new FileWriter(resource.getFile());
          
        	Map<String, Map<String, String>> tablesAndColumns = null;
        	
            if (dataBaseType.equals("mysql")) {
            	tablesAndColumns = mySqlDataBaseRepository.getTablesAndColumnsFromDataBase();
            } else if (dataBaseType.equals("oracle")) {
            	tablesAndColumns = oracleDataBaseRepository.getTablesAndColumnsFromDataBase();
            } else {
        		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
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
        	objectMapper = new ObjectMapper();
        	fileWriter = new FileWriter(resource.getFile());
        	ArrayList<RelationshipData> relationships = null;
        	
        	if (dataBaseType.equals("mysql")) {
        		relationships = mySqlDataBaseRepository.getRelationshipsFromDataBase();
        	} else if (dataBaseType.equals("oracle")) {
        		relationships = oracleDataBaseRepository.getRelationshipsFromDataBase();
        	} else {
        		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
        	}
        	String json = objectMapper.writeValueAsString(relationships);
        	fileWriter.write(json);
        	fileWriter.close();
        } else {
        	throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }
    
    private TreatedLoadedQueryData treatLoadedQueryData(LoadedQueryData loadedQueryData, List<ColumnAndTotalizer> totalizers) {
    	ArrayList<String> columnsNameOrNickName = columnsNameAndNickNameToColumnsNameOrNickName(loadedQueryData.columnsNameAndNickName());
    	Map<String, String> columnsAndTotalizersResult = null;
    	
    	if (totalizers != null) {
    		columnsAndTotalizersResult = joinColumnsAndTotalizersResult(loadedQueryData, totalizers);
    	}
    	
    	return new TreatedLoadedQueryData(columnsNameOrNickName, loadedQueryData.foundObjects(), columnsAndTotalizersResult);
    }
    
    private Map<String, String> joinColumnsAndTotalizersResult(LoadedQueryData loadedQueryData, List<ColumnAndTotalizer> totalizers) {
    	int totalizersResultsCounter = 0;
        Map<String, String> columnsAndTotalizersResult = new HashMap<>();
        
        for (ColumnAndTotalizer columnAndTotalizer : totalizers) {
        	Entry<String, Totalizer> totalizerAndColumn = columnAndTotalizer.totalizers().entrySet().iterator().next();
        	String columnsAndTotalizersColumn = null;
        	
        	for (Map.Entry<String, String> columnNameAndNickName : loadedQueryData.columnsNameAndNickName().entrySet()) {
        		
        		if (totalizerAndColumn.getKey().equalsIgnoreCase(columnNameAndNickName.getKey())) {
        			if (columnNameAndNickName.getValue() != null) {
        				columnsAndTotalizersColumn = columnNameAndNickName.getValue();
        			} else {
        				columnsAndTotalizersColumn = columnNameAndNickName.getKey();
        			}
        		}
        	}
        	columnsAndTotalizersResult.put(columnsAndTotalizersColumn, totalizerAndColumn.getValue().toPortuguese() + ": " + loadedQueryData.totalizersResult().get(totalizersResultsCounter));
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