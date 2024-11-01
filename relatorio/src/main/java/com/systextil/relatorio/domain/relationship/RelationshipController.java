package com.systextil.relatorio.domain.relationship;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.infra.exception_handler.CannotConnectToDataBaseException;

@RestController
@RequestMapping("relationships")
public class RelationshipController {
	
	@Value("${relationships.json.file.path}")
    private String relationshipsJsonPath;
    
    @Value("${relationships_with_joins.json.file.path}")
    private String relationshipsWithJoinsJsonPath;

    @Value("${database.type}")
    private String dataBaseType;
	
    private static final String FILE_NOT_FOUND_MESSAGE = "Arquivo não encontrado ou não legível: ";
    private static final String NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE = "Tipo do banco de dados não configurado";
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";

	@GetMapping
    public ResponseEntity<Resource> getRelationships() throws IOException {
        Path filePath = Paths.get(relationshipsJsonPath);
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
    public void setRelationshipsFromDatabaseIntoJson() throws SQLException, IOException {
        Path filePath = Paths.get(relationshipsJsonPath);
        Path fileOfJoinsPath = Paths.get(relationshipsWithJoinsJsonPath);
        Resource resource = new UrlResource(filePath.toUri());
        Resource resourceWithJoins = new UrlResource(fileOfJoinsPath.toUri());
        
        if (resourceWithJoins.isReadable() && resourceWithJoins.exists()) {
        	ObjectMapper objectMapper = new ObjectMapper();
        	List<RelationshipData> relationships = null;
        	
        	if (dataBaseType.equals(MYSQL)) {
        		relationships = new MysqlRepository().getRelationshipsFromDataBase();
        	} else if (dataBaseType.equals(ORACLE)) {
        		relationships = new OracleRepository().getRelationshipsFromDataBase();
        	} else {
        		throw new CannotConnectToDataBaseException(NOT_CONFIGURED_DATA_BASE_TYPE_MESSAGE);
        	}
        	
        	try(FileWriter fileWriter = new FileWriter(resourceWithJoins.getFile())) {
        		fileWriter.write(objectMapper.writeValueAsString(relationships));
        	}
        	ArrayList<String> tables = new ArrayList<>();
        	
        	for (RelationshipData relationship : relationships) {
        		tables.add(relationship.tablesPair());
        	}
        	
        	try(FileWriter fileWriter = new FileWriter(resource.getFile())) {
        		fileWriter.write(objectMapper.writeValueAsString(tables));
        	}
        } else {
        	throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }
}