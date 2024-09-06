package com.systextil.relatorio.dataBaseData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.service.SQLGenerator;
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
        String sql = SQLGenerator.finalQuery(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        ArrayList<String> columnsNickName = loadQuery(sql).columnsNickName();
        ArrayList<Object[]> foundObjects = loadQuery(sql).foundObjects();

        return new Object[]{sql, columnsNickName, foundObjects};
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
    public LoadedQueryData loadQuery(@RequestBody String sql) throws RuntimeException {
        dataBaseDataRepository = new DataBaseDataRepository();
        LoadedQueryData loadedQueryData;
        
        try {
            loadedQueryData = dataBaseDataRepository.findDataByQueryFromMySQLDatabase(sql);
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
