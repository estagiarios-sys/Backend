package com.systextil.relatorio.domain.relationship;

import java.sql.SQLException;
import java.util.List;

import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

class OracleRepository extends RelationshipRepository {
	
	List<RelationshipData> getRelationshipsFromDataBase() throws SQLException {
        OracleConnection connectionOracle = new OracleConnection();
        connectionOracle.connect();
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
                
        List<RelationshipData> listRelationshipData = super.getRelationshipsFromDataBase(connectionOracle.getIdConnection(), sql);
        connectionOracle.disconnect();
        
        return listRelationshipData;
    }
}