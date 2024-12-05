package com.systextil.relatorio.domain.table;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.systextil.relatorio.infra.exception_handler.IllegalDataBaseTypeException;

@Service
class TableService {

    private final String dataBaseType;
    private final TableOracleRepository oracleRepository;
    private final TableMysqlRepository mysqlRepository;
    private final TableStorageAccessor storageAccessor;

    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
    
    TableService(
    		TableMysqlRepository mysqlRepository,
    		TableOracleRepository oracleRepository,
    		TableStorageAccessor storageAccessor,
    		@Value("${database.type}") String dataBaseType
    		) {
    	this.oracleRepository = oracleRepository;
    	this.mysqlRepository = mysqlRepository;
    	this.storageAccessor = storageAccessor;
    	this.dataBaseType = dataBaseType;
    }
	
	Resource getTables() throws IOException {
    	return storageAccessor.getTables();
    }
	
	Map<String, Map<String, String>> getColumnsFromTables(AllTables allTables) throws SQLException {
		Map<String, Map<String, String>> tablesAndColumns = new LinkedHashMap<>();
		
		if (dataBaseType.equals(MYSQL)) {
			mysqlRepository.getColumnsFromTable(allTables.mainTable());
		} else if (dataBaseType.equals(ORACLE)) {
			tablesAndColumns.put(allTables.mainTable(), oracleRepository.getColumnsFromTables(allTables.mainTable()));
			
			for (String tablesPair : allTables.tablesPairs()) {
				Pattern pattern = Pattern.compile("\\b\\w+$");
				Matcher matcher = pattern.matcher(tablesPair);
				matcher.find();
				String table = matcher.group();
				tablesAndColumns.put(table, oracleRepository.getColumnsFromTables(table));
			}
		} else {
			throw new IllegalDataBaseTypeException(dataBaseType);
		}
		return tablesAndColumns;
	}
	
	void setTablesIntoJson() throws IOException, SQLException {
        List<String> tablesAndColumns = null;

        if (dataBaseType.equals(MYSQL)) {
        	tablesAndColumns = mysqlRepository.getTables();
        } else if (dataBaseType.equals(ORACLE)) {
        	tablesAndColumns = oracleRepository.getTables();
        } else {
        	throw new IllegalDataBaseTypeException(dataBaseType);
        }
        storageAccessor.setTablesIntoJson(tablesAndColumns);
    }
}