package com.systextil.relatorio.domain.table;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.infra.exception_handler.CannotConnectToDataBaseException;

@RestController
@RequestMapping("tables")
public class TableController {
	
    @Value("${tables.json.file.path}")
	private String tablesJsonFilePath;
    
    @Value("${database.type}")
    private String dataBaseType;
	
    private static final String FILE_NOT_FOUND_MESSAGE = "Arquivo não encontrado ou não legível: ";
    private static final String NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE = "Tipo do banco de dados não configurado";
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
    
	@GetMapping
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

    @PutMapping
    public void setTablesFromDatabaseIntoJson() throws IOException, SQLException {
        Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	ObjectMapper objectMapper = new ObjectMapper();          
        	Map<String, Map<String, String>> tablesAndColumns = null;
        	
            if (dataBaseType.equals(MYSQL)) {
            	tablesAndColumns = new MysqlRepository().getTablesFromDataBase();
            } else if (dataBaseType.equals(ORACLE)) {
            	tablesAndColumns = new OracleRepository().getTablesFromDataBase();
            } else {
        		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
            }
            String json = objectMapper.writeValueAsString(tablesAndColumns);
            
            try(FileWriter fileWriter = new FileWriter(resource.getFile())) {
            	fileWriter.write(json);
            }
        } else {
        	throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }	
}