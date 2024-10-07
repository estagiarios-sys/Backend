package com.systextil.relatorio.domain.data_base_data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.domain.ColumnAndTotalizer;
import com.systextil.relatorio.domain.TotalizerTypes;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("find")
public class DataBaseDataController {

    private final OracleDataBaseDataRepository oracleRepository;
    private final MySqlDataBaseDataRepository mySqlRepository;
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
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
    
    public DataBaseDataController(OracleDataBaseDataRepository oracleDataBaseRepository, MySqlDataBaseDataRepository mySqlDataBaseRepository) {
    	this.oracleRepository = oracleDataBaseRepository;
    	this.mySqlRepository = mySqlDataBaseRepository;
    }
    
    @PostMapping
    public Object[] getQueryReturn(@RequestBody @Valid QueryData queryData) throws SQLException, ParseException {
        String finalQuery = SqlGenerator.generateFinalQuery(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        
        if (dataBaseType.equals(ORACLE)) {
        	finalQuery = SqlWithDateConverter.toSqlWithDdMMMyyyy(finalQuery);
        }
        String totalizersQuery = null;
        
        if (!queryData.totalizers().isEmpty()) {
        	totalizersQuery = SqlGenerator.generateTotalizersQuery(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        }
        List<ColumnAndTotalizer> totalizers = new ArrayList<>();
        
        for (Map.Entry<String, TotalizerTypes> entry : queryData.totalizers().entrySet()) {
        	totalizers.add(new ColumnAndTotalizer(Map.of(entry.getKey(), entry.getValue())));
        }
        ToLoadQueryData toLoadQueryData = new ToLoadQueryData(finalQuery, totalizersQuery, totalizers);
        LoadedQueryData loadedQueryData = null;
        
        if (dataBaseType.equals(MYSQL)) {
        	loadedQueryData = mySqlRepository.findDataByQuery(toLoadQueryData.finalQuery(), toLoadQueryData.totalizersQuery());
        } else if (dataBaseType.equals(ORACLE)) {
        	loadedQueryData = oracleRepository.findDataByQuery(toLoadQueryData.finalQuery(), toLoadQueryData.totalizersQuery());
        } else {
    		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
        }
        
        TreatedLoadedQueryData treatedLoadedQueryData = LoadedQueryDataTreater.treatLoadedQueryData(loadedQueryData, totalizers);
        ArrayList<String> columnsNameOrNickName = treatedLoadedQueryData.columnsNameOrNickName();
        ArrayList<Object[]> foundObjects = treatedLoadedQueryData.foundObjects();
        Map<String, String> columnsAndTotalizersResult = treatedLoadedQueryData.columnsAndTotalizersResult();
        
        return new Object[]{finalQuery, totalizersQuery, columnsNameOrNickName, foundObjects, columnsAndTotalizersResult};
    }
    
    @PostMapping("analysis")
    public double getQueryAnalysis(@RequestBody @Valid QueryData queryData) throws SQLException {
    	int actualTime = 0;
    	
    	if (dataBaseType.equals(MYSQL)) {
    		String finalQueryAnalysis = SqlGenerator.generateFinalQueryAnalysisFromMySQLDataBase(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        	String totalizersQueryAnalysis = null;
        	
        	if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromMySQLDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        	}
        	actualTime = mySqlRepository.getActualTimeFromQueriesAnalysisFromDataBase(finalQueryAnalysis, totalizersQueryAnalysis);
    	} else if (dataBaseType.equals(ORACLE)) {
    		String[] finalQueryAnaysis = SqlGenerator.generateFinalQueryAnalysisFromOracleDataBase(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
    		String[] totalizersQueryAnalysis = null;
    		
    		if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromOracleDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        	}
    		actualTime = oracleRepository.getActualTimeFromQueriesAnalysisFromDataBase(finalQueryAnaysis, totalizersQueryAnalysis);
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

    @PutMapping("update/table")
    public void setTablesAndColumnsFromDatabaseIntoJson() throws IOException, SQLException {
        Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	objectMapper = new ObjectMapper();
        	fileWriter = new FileWriter(resource.getFile());
          
        	Map<String, Map<String, String>> tablesAndColumns = null;
        	
            if (dataBaseType.equals(MYSQL)) {
            	tablesAndColumns = mySqlRepository.getTablesAndColumnsFromDataBase();
            } else if (dataBaseType.equals(ORACLE)) {
            	tablesAndColumns = oracleRepository.getTablesAndColumnsFromDataBase();
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
        	
        	if (dataBaseType.equals(MYSQL)) {
        		relationships = mySqlRepository.getRelationshipsFromDataBase();
        	} else if (dataBaseType.equals(ORACLE)) {
        		relationships = oracleRepository.getRelationshipsFromDataBase();
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
}