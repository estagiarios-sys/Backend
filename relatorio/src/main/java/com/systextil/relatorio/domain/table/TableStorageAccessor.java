package com.systextil.relatorio.domain.table;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
class TableStorageAccessor {

	private final String tablesJsonFilePath;
	
	private static final String FILE_NOT_FOUND_MESSAGE = "Arquivo não encontrado ou não legível: ";
	
	TableStorageAccessor(@Value("${tables.json.file.path}") String tablesJsonFilePath) {
		this.tablesJsonFilePath = tablesJsonFilePath;
	}
	
	Resource getTables() throws IOException {
    	Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }
	
	void setTablesIntoJson(List<String> tablesAndColumns) throws IOException {
        Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(tablesAndColumns);
            
            try(FileWriter fileWriter = new FileWriter(resource.getFile())) {
            	fileWriter.write(json);
            }
        } else {
        	throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }
}