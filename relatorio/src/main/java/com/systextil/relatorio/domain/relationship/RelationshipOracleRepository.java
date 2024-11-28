package com.systextil.relatorio.domain.relationship;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

@Repository
class RelationshipOracleRepository {
	
	private final OracleConnection connection;
	private final RelationshipRepository repository;
	
	RelationshipOracleRepository(OracleConnection connection, RelationshipRepository repository) {
		this.connection = connection;
		this.repository = repository;
	}
	
	List<RelationshipData> getRelationshipsFromDataBase() throws SQLException {
        connection.connect();
        String sql = "SELECT " +
                "  uc.TABLE_NAME, " +
                "  uc.COLUMN_NAME, " +
                "  uc.CONSTRAINT_NAME, " +
                "  rc.TABLE_NAME AS REFERENCED_TABLE_NAME, " +
                "  rc.COLUMN_NAME AS REFERENCED_COLUMN_NAME " +
                "FROM " +
                "  USER_CONS_COLUMNS uc " +
                "JOIN " +
                "  USER_CONSTRAINTS c ON uc.CONSTRAINT_NAME = c.CONSTRAINT_NAME " +
                "JOIN " +
                "  USER_CONS_COLUMNS rc ON c.R_CONSTRAINT_NAME = rc.CONSTRAINT_NAME " +
                "WHERE " +
                "  c.CONSTRAINT_TYPE = 'R' " +
                "AND uc.POSITION = rc.POSITION " +
                "ORDER BY " +
                "  uc.TABLE_NAME, uc.COLUMN_NAME";
                
        List<RelationshipData> listRelationshipData = repository.getRelationshipsFromDataBase(connection.getIdConnection(), sql);
        connection.disconnect();
        
        return listRelationshipData;
    }
}