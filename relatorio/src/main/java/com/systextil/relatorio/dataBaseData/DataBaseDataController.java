package com.systextil.relatorio.dataBaseData;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    
    @Value("${tables.json.file.path}")
	private String tablesJsonFilePath;
    
    @Value("${relationships.json.file.path}")
    private String relationshipsJsonFilePath;

    @PostMapping
    public Object[] getQueryReturn(@RequestBody @Valid QueryData queryData) throws RuntimeException {
        String finalQuery = SQLGenerator.generateFinalQuery(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        QueryWithTotalizers queryWithTotalizers = SQLGenerator.generateTotalizersQuery(queryData.totalizers(), queryData.table());
        
        LoadedQueryData loadedQueryData = loadQuery(finalQuery, queryWithTotalizers);
        ArrayList<String> columnsNickName = loadedQueryData.columnsNickName();
        ArrayList<Object[]> foundObjects = loadedQueryData.foundObjects();
        ArrayList<String> totalizersResults = loadedQueryData.totalizersResults();
        int index = 0;
        Map<String, String> columnAndTotalizer = new HashMap<String, String>();
        
        for (Map.Entry<String, Totalizer> totalizer : queryData.totalizers().entrySet()) {
        	columnAndTotalizer.put(totalizer.getKey(), totalizersResults.get(index));
        	index++;
        }
        
        return new Object[]{finalQuery, queryWithTotalizers.query(), columnsNickName, foundObjects, columnAndTotalizer};
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
            throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath);
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
            throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath);
        }
    }
    
    @PostMapping("loadedQuery")
    public LoadedQueryData loadQuery(@RequestBody String finalQuery, QueryWithTotalizers queryWithTotalizers) throws RuntimeException {
        dataBaseDataRepository = new DataBaseDataRepository();
        LoadedQueryData loadedQueryData;
        
        try {
            loadedQueryData = dataBaseDataRepository.findDataByQueryFromMySQLDatabase(finalQuery, queryWithTotalizers);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        return loadedQueryData;
    }
    
    @PutMapping("update/table")
    public void setTablesAndColumnsFromDatabaseIntoJson() throws Exception {
        Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	dataBaseDataRepository = new DataBaseDataRepository();
        	ObjectMapper objectMapper = new ObjectMapper();
        	FileWriter fileWriter = new FileWriter(resource.getFile());
            Map<String, String[]> tablesAndColumns = dataBaseDataRepository.getTablesAndColumnsFromMySQLDatabase();
            String json = objectMapper.writeValueAsString(tablesAndColumns);
        	fileWriter.write(json);
        	fileWriter.close();
        } else {
        	throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath);
        }
    }

    @PutMapping("update/relationship")
    public void setRelationshipsFromDatabaseIntoJson() throws SQLException, ClassNotFoundException, IOException {
        Path filePath = Paths.get(relationshipsJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	dataBaseDataRepository = new DataBaseDataRepository();
        	ObjectMapper objectMapper = new ObjectMapper();
        	FileWriter fileWriter = new FileWriter(resource.getFile());
        	ArrayList<RelationshipData> relationships = dataBaseDataRepository.getRelationshipsFromMySQLDatabase();
        	String json = objectMapper.writeValueAsString(relationships);
        	fileWriter.write(json);
        	fileWriter.close();
        } else {
        	throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath);
        }
    }
}
