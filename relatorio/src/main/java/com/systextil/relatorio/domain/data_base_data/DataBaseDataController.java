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
import java.nio.file.Files;
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
    
    @Value("${relationships_with_joins.json.file.path}")
    private String relationshipsWithJoinsJsonFilePath;
    
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
    public Object[] getQueryReturn(@RequestBody @Valid QueryData queryData) throws SQLException, ParseException, IOException {
        String finalQuery = SqlGenerator.generateFinalQuery(queryData.table(), joinColumnsNameAndNickName(queryData.columns()), queryData.conditions(), queryData.orderBy(), findJoinsByTablesPairs(queryData.tablesPairs()));
        
        if (dataBaseType.equals(ORACLE)) {
        	finalQuery = SqlWithDateConverter.toSqlWithDdMMMyyyy(finalQuery);
        }
        String totalizersQuery = null;
        
        if (!queryData.totalizers().isEmpty()) {
        	totalizersQuery = SqlGenerator.generateTotalizersQuery(queryData.totalizers(), queryData.table(), queryData.conditions(), findJoinsByTablesPairs(queryData.tablesPairs()));
        }
        List<ColumnAndTotalizer> totalizers = new ArrayList<>();
        
        for (Map.Entry<String, TotalizerTypes> entry : queryData.totalizers().entrySet()) {
        	totalizers.add(new ColumnAndTotalizer(Map.of(entry.getKey(), entry.getValue())));
        }
        LoadedQueryData loadedQueryData = null;
        
        if (dataBaseType.equals(MYSQL)) {
        	loadedQueryData = mySqlRepository.findDataByQuery(finalQuery, totalizersQuery);
        } else if (dataBaseType.equals(ORACLE)) {
        	loadedQueryData = oracleRepository.findDataByQuery(finalQuery, totalizersQuery);
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
    public double getQueryAnalysis(@RequestBody @Valid QueryData queryData) throws SQLException, IOException {
    	int actualTime = 0;
    	
    	if (dataBaseType.equals(MYSQL)) {
    		String finalQueryAnalysis = SqlGenerator.generateFinalQueryAnalysisFromMySQLDataBase(queryData.table(), joinColumnsNameAndNickName(queryData.columns()), queryData.conditions(), queryData.orderBy(), findJoinsByTablesPairs(queryData.tablesPairs()));
        	String totalizersQueryAnalysis = null;
        	
        	if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromMySQLDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), findJoinsByTablesPairs(queryData.tablesPairs()));
        	}
        	actualTime = mySqlRepository.getActualTimeFromQueriesAnalysisFromDataBase(finalQueryAnalysis, totalizersQueryAnalysis);
    	} else if (dataBaseType.equals(ORACLE)) {
    		String[] finalQueryAnaysis = SqlGenerator.generateFinalQueryAnalysisFromOracleDataBase(queryData.table(), joinColumnsNameAndNickName(queryData.columns()), queryData.conditions(), queryData.orderBy(), findJoinsByTablesPairs(queryData.tablesPairs()));
    		String[] totalizersQueryAnalysis = null;
    		
    		if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SqlGenerator.generateTotalizersQueryAnalysisFromOracleDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), findJoinsByTablesPairs(queryData.tablesPairs()));
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
        Path fileOfJoinsPath = Paths.get(relationshipsWithJoinsJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        Resource resourceWithJoins = new UrlResource(fileOfJoinsPath.toUri());
        
        if (resourceWithJoins.isReadable() && resourceWithJoins.exists()) {
        	objectMapper = new ObjectMapper();
        	fileWriter = new FileWriter(resourceWithJoins.getFile());
        	ArrayList<RelationshipData> relationships = null;
        	
        	if (dataBaseType.equals(MYSQL)) {
        		relationships = mySqlRepository.getRelationshipsFromDataBase();
        	} else if (dataBaseType.equals(ORACLE)) {
        		relationships = oracleRepository.getRelationshipsFromDataBase();
        	} else {
        		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
        	}
        	fileWriter.write(objectMapper.writeValueAsString(relationships));
        	fileWriter.close();
        	ArrayList<String> tables = new ArrayList<>();
        	for (RelationshipData relationship : relationships) {
        		tables.add(relationship.tablesPair());
        	}
        	fileWriter = new FileWriter(resource.getFile());
        	fileWriter.write(objectMapper.writeValueAsString(tables));
        	fileWriter.close();
        } else {
        	throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
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
    	objectMapper = new ObjectMapper();
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