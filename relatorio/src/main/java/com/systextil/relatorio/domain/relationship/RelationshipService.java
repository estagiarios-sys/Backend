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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.infra.exception_handler.IllegalDataBaseTypeException;

@Service
class RelationshipService {

	@Value("${relationships.json.file.path}")
    private String relationshipsJsonPath;
    
    @Value("${relationships_with_joins.json.file.path}")
    private String relationshipsWithJoinsJsonPath;

    @Value("${database.type}")
    private String dataBaseType;
	
    private static final String FILE_NOT_FOUND_MESSAGE = "Arquivo não encontrado ou não legível: ";
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
	
	Resource getRelationships() throws IOException {
        Path filePath = Paths.get(relationshipsJsonPath);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }
	
	void setRelationshipsFromDatabaseIntoJson() throws SQLException, IOException {
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
        		throw new IllegalDataBaseTypeException();
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