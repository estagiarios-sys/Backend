package com.systextil.relatorio.domain.relationship;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.systextil.relatorio.domain.RelationshipData;

public class RelationshipRepository {
	
    List<RelationshipData> getRelationshipsFromDataBase(Connection idConnection, String sql) throws SQLException {
    	List<RelationshipData> listRelationshipData = new ArrayList<>();
    	
    	try(PreparedStatement command = idConnection.prepareStatement(sql)) {
    		ResultSet data = command.executeQuery();

            while (data.next()) {
                String tableName = data.getString("TABLE_NAME");
                String columnName = data.getString("COLUMN_NAME");
                String referencedTableName = data.getString("REFERENCED_TABLE_NAME");
                String referencedColumnName = data.getString("REFERENCED_COLUMN_NAME");
                String tableAndReferencedTable = tableName + " e " + referencedTableName;
                String join = "INNER JOIN " + referencedTableName + " ON " + tableName + "." + columnName + " = " + referencedTableName + "." + referencedColumnName;
                RelationshipData relationshipData = new RelationshipData(tableAndReferencedTable, join);
                listRelationshipData.add(relationshipData);
                String tableAndReferencedTableReversed = referencedTableName + " e " + tableName;
                String joinReversed = "INNER JOIN " + tableName + " ON " + referencedTableName + "." + referencedColumnName + " = " + tableName + "." + columnName;
                RelationshipData relationshipDataReversed = new RelationshipData(tableAndReferencedTableReversed, joinReversed);
                listRelationshipData.add(relationshipDataReversed);
            }
    	}
        
        return listRelationshipData;
    }
}