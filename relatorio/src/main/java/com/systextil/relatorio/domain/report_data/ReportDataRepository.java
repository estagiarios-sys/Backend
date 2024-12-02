package com.systextil.relatorio.domain.report_data;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

@Repository
class ReportDataRepository {

    ReportData findDataByFinalQuery(Connection idConnection, String sql) throws SQLException {
        List<Object[]> listObjects = new ArrayList<>();
        Map<String, String> columnsNameAndNickName = new LinkedHashMap<>();
        List<String> tableNames = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        Pattern tableDotColumnPattern = Pattern.compile("(\\w+)\\.(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher tableDotColumnMatcher = tableDotColumnPattern.matcher(sql);
        
        while (tableDotColumnMatcher.find()) {
            String tableName = tableDotColumnMatcher.group(1);
            String columnName = tableDotColumnMatcher.group(2);
            tableNames.add(tableName);
            columnNames.add(columnName);
        }
        try(PreparedStatement command = idConnection.prepareStatement(sql)) {
        	ResultSet data = command.executeQuery();
            ResultSetMetaData metaData = data.getMetaData();
            int columnsNumber = metaData.getColumnCount();

            for (int i = 1; i <= columnsNumber; i++) {
                String columnNickName = metaData.getColumnLabel(i);
                String columnName = columnNames.get(i-1);

                if (columnNickName.equalsIgnoreCase(columnName)) {
                    columnsNameAndNickName.put(tableNames.get(i-1) + "." + columnName, null);
                } else {
                    columnsNameAndNickName.put(tableNames.get(i-1) + "." + columnName, columnNickName);
                }
            }
            while (data.next()) {
                Object[] object = new Object[columnsNumber];

                for (int i = 1; i <= columnsNumber; i++) {
                    object[i - 1] = data.getString(i);
                }
                listObjects.add(object);
            }
        }
        return new ReportData(columnsNameAndNickName, listObjects, null);
    }
    
    List<String> findDataByTotalizersQuery(Connection idConnection, String totalizersQuery) throws SQLException {
    	List<String> totalizersResults = new ArrayList<>();
    	
    	try(PreparedStatement command = idConnection.prepareStatement(totalizersQuery)) {
    		ResultSet data = command.executeQuery();
        	ResultSetMetaData metaData = data.getMetaData();
        	int columnsNumber = metaData.getColumnCount();
        	data.next();
        	
        	for (int i = 1; i <= columnsNumber; i++) {
        		totalizersResults.add(String.valueOf(data.getInt(i)));
        	}
    	}	
    	return totalizersResults;
    }
}