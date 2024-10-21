package com.systextil.relatorio.domain.relationship;

import java.sql.SQLException;
import java.util.List;

import com.systextil.relatorio.domain.RelationshipData;
import com.systextil.relatorio.infra.data_base_connection.MysqlConnection;

class MysqlRepository extends RelationshipRepository {
	
	List<RelationshipData> getRelationshipsFromDataBase() throws SQLException {
        MysqlConnection connectionMySQL = new MysqlConnection();
        connectionMySQL.connect();
        String sql = "SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE REFERENCED_TABLE_NAME IS NOT NULL";
        List<RelationshipData> listRelationshipData = super.getRelationshipsFromDataBase(connectionMySQL.getIdConnection(), sql);
        connectionMySQL.disconnect();
        
        return listRelationshipData;
    }
}