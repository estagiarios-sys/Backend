package com.systextil.relatorio.domain.table;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.infra.exception_handler.IllegalDataBaseTypeException;

@Service
class TableService {
	
	@Value("${tables.json.file.path}")
	private String tablesJsonFilePath;
    
    @Value("${database.type}")
    private String dataBaseType;
    
    private MysqlRepository mysqlRepository;
    private OracleRepository oracleRepository;
	
    private static final String FILE_NOT_FOUND_MESSAGE = "Arquivo não encontrado ou não legível: ";
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
	
	Resource getTables() throws IOException {
    	Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE + filePath);
        }
    }
	
	Map<String, Map<String, String>> getColumnsFromTables(AllTables allTables) throws SQLException {
		oracleRepository = new OracleRepository();
		
		if (dataBaseType.equals(MYSQL)) {
			return mysqlRepository.getColumnsFromTables(allTables);
		} else if (dataBaseType.equals(ORACLE)) {
			return oracleRepository.getColumnsFromTables(allTables);
		} else {
			throw new IllegalDataBaseTypeException();
		}
	}
	
	void setTablesIntoJson() throws IOException, SQLException {
    	mysqlRepository = new MysqlRepository();
    	oracleRepository = new OracleRepository();
        Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	ObjectMapper objectMapper = new ObjectMapper();          
        	List<String> tablesAndColumns = null;
        	
            if (dataBaseType.equals(MYSQL)) {
            	tablesAndColumns = mysqlRepository.getTables();
            } else if (dataBaseType.equals(ORACLE)) {
            	tablesAndColumns = oracleRepository.getTables();
            } else {
        		throw new IllegalDataBaseTypeException();
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