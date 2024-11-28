package com.systextil.relatorio.domain.relationship;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.infra.data_base_connection.MysqlConnection;

@Repository
class RelationshipMysqlRepository {
	
	private final MysqlConnection connection;
	private final RelationshipRepository repository;
	
	RelationshipMysqlRepository(MysqlConnection connection, RelationshipRepository repository) {
		this.connection = connection;
		this.repository = repository;
	}
	
	List<RelationshipData> getRelationshipsFromDataBase() throws SQLException {
        connection.connect();
        String sql = "SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE REFERENCED_TABLE_NAME IS NOT NULL";
        List<RelationshipData> listRelationshipData = repository.getRelationshipsFromDataBase(connection.getIdConnection(), sql);
        connection.disconnect();
        
        return listRelationshipData;
    }
}