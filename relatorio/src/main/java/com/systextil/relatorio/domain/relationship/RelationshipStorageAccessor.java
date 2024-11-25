package com.systextil.relatorio.domain.relationship;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.domain.RelationshipData;

@Component
class RelationshipStorageAccessor {
	
	@Value("${relationships.json.file.path}")
    private String relationshipsJsonPath;
    
    @Value("${relationships_with_joins.json.file.path}")
    private String relationshipsWithJoinsJsonPath;
    
    private static final String FILE_NOT_FOUND_MESSAGE = "Arquivo não encontrado ou não legível: ";

    private final ObjectMapper objectMapper;

    RelationshipStorageAccessor() {
    	this.objectMapper = new ObjectMapper();
	}
    
    Resource getRelationships() throws FileNotFoundException, MalformedURLException {
    	Path filePath = Paths.get(relationshipsJsonPath);
        Resource resource = new UrlResource(filePath.toUri());
    	
    	if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    	return resource;
    }

    void setRelationshipsIntoJson(List<String> tablesPairs) throws IOException {
    	Path filePath = Paths.get(relationshipsJsonPath);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    	try(FileWriter fileWriter = new FileWriter(resource.getFile())) {
    		fileWriter.write(objectMapper.writeValueAsString(tablesPairs));
    	}
    }
    
    void setRelationshipsWithJoinsIntoJson(List<RelationshipData> relationships) throws IOException {
    	Path fileOfJoinsPath = Paths.get(relationshipsWithJoinsJsonPath);
        Resource resourceWithJoins = new UrlResource(fileOfJoinsPath.toUri());
        
        if (!resourceWithJoins.exists() || !resourceWithJoins.isReadable()) {
        	throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + fileOfJoinsPath);
        }    	
    	try(FileWriter fileWriter = new FileWriter(resourceWithJoins.getFile())) {
    		fileWriter.write(objectMapper.writeValueAsString(relationships));
    	}
    }
}