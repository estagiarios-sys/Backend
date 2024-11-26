package com.systextil.relatorio.domain.relationship;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.infra.exception_handler.IllegalDataBaseTypeException;

@Service
class RelationshipService {

    @Value("${database.type}")
    private String dataBaseType;
	
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
    
    private final MultiJoinReducer multiJoinReducer;
    private final RelationshipStorageAccessor storageAccessor;
    
    RelationshipService(MultiJoinReducer multiJoinReducer, RelationshipStorageAccessor storageAccessor) {
		this.multiJoinReducer = multiJoinReducer;
		this.storageAccessor = storageAccessor;
	}
    
    Resource getRelationships() throws FileNotFoundException, MalformedURLException {
    	return storageAccessor.getRelationships();
    }

	void setRelationshipsIntoJson() throws SQLException, IOException {
        List<RelationshipData> impreciseRelationships = null;

        if (dataBaseType.equals(MYSQL)) {
        	impreciseRelationships = new MysqlRepository().getRelationshipsFromDataBase();
        } else if (dataBaseType.equals(ORACLE)) {
        	impreciseRelationships = new OracleRepository().getRelationshipsFromDataBase();
        } else {
        	throw new IllegalDataBaseTypeException(dataBaseType);
        }
        List<String> tablesPairs = new ArrayList<>();

        for (RelationshipData relationship : impreciseRelationships) {
        	tablesPairs.add(relationship.tablesPair());
        }
        List<String> duplicates = multiJoinReducer.findDuplicates(tablesPairs);
        List<RelationshipData> relationships = multiJoinReducer.cutDuplicates(duplicates, impreciseRelationships);
        tablesPairs = new ArrayList<>();

        for (RelationshipData relationship : relationships) {
        	tablesPairs.add(relationship.tablesPair());
        }
        storageAccessor.setRelationshipsIntoJson(tablesPairs);
        storageAccessor.setRelationshipsWithJoinsIntoJson(relationships);
    }
}