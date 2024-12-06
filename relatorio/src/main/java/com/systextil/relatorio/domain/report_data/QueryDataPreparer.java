package com.systextil.relatorio.domain.report_data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.systextil.relatorio.domain.RelationshipData;

@Component
class QueryDataPreparer {
	
	private final ReportDataStorageAccessor storageAccessor;
	
	QueryDataPreparer(ReportDataStorageAccessor storageAccessor) {
		this.storageAccessor = storageAccessor;
	}

	List<String> findJoinsByTablesPairs(String mainTable, List<String> tablesPairs) throws IOException {
		List<String> joins = new ArrayList<>();
    	List<RelationshipData> relationshipsData = storageAccessor.findRelationshipData();

    	for (int mainIndex = 0; mainIndex < tablesPairs.size(); mainIndex++) {
    		for (RelationshipData relationshipData : relationshipsData) {
    			String tablesPair = tablesPairs.get(mainIndex);
    			
    			if (tablesPair.equals(relationshipData.tablesPair())) {
    				StringBuilder joinBuilder = new StringBuilder();
    				joinBuilder.append(relationshipData.join());
    				
    				if (mainIndex > 0) {
    					Pattern firstTablePattern = Pattern.compile("^\\w+\\b");
    					Matcher firstTableMatcher = firstTablePattern.matcher(tablesPair);
    					firstTableMatcher.find();
    					String firstTable = firstTableMatcher.group();
    					Pattern lastTablePattern = Pattern.compile("\\b\\w+$");
    					Matcher lastTableMatcher = lastTablePattern.matcher(tablesPair);
    					lastTableMatcher.find();
    					String lastTable = lastTableMatcher.group();
    					
    					joinBuilder.append(compareToMainTable(mainTable, firstTable, lastTable, relationshipsData));
    					joinBuilder.append(compareToPreviousTablesPairs(lastTablePattern, mainIndex, firstTable, lastTable, tablesPairs, relationshipsData));
    				}
    				joins.add(joinBuilder.toString());
    			}
    		}
    	}
    	return joins;
    }

	List<String> joinColumnsNameAndNickName(List<QueryDataColumn> columns) {
    	List<String> joinedColumnsNameAndNickName = new ArrayList<>();
    	
    	for (QueryDataColumn column : columns) {
    		if (column.nickName() == null || column.nickName().isBlank()) {
    			joinedColumnsNameAndNickName.add(column.name());
    		} else {
    			joinedColumnsNameAndNickName.add(column.name() + " AS \"" + column.nickName() + "\"");
    		}
    	}
    	
    	return joinedColumnsNameAndNickName;
    }
	
	private String compareToMainTable(String mainTable, String firstTable, String lastTable, List<RelationshipData> relationshipsData) {
		if (!firstTable.equals(mainTable)) {
			return findJoinsByTable(lastTable, mainTable, relationshipsData);
		}
		return "";
	}
	
	private String compareToPreviousTablesPairs(Pattern lastTablePattern, int mainIndex, String firstTable, String lastTable, List<String> tablesPairs, List<RelationshipData> relationshipsData) {
		StringBuilder joinBuilder = new StringBuilder();
		
		for (int secondaryIndex = 0; secondaryIndex < mainIndex; secondaryIndex++) {			
			Matcher lastTableMatcher = lastTablePattern.matcher(tablesPairs.get(secondaryIndex));
			lastTableMatcher.find();
			String previousLastTable = lastTableMatcher.group();
			
			if (!firstTable.equals(previousLastTable)) {
				joinBuilder.append(findJoinsByTable(lastTable, previousLastTable, relationshipsData));
			}
		}
		return joinBuilder.toString();
	}
	
	private String findJoinsByTable(String table, String comparedTable, List<RelationshipData> relationshipsData) {
		StringBuilder joinBuilder = new StringBuilder();
		Pattern pattern = Pattern.compile(table + " e " + comparedTable);
    	
    	for (RelationshipData relationshipData : relationshipsData) {
			Matcher matcher = pattern.matcher(relationshipData.tablesPair());
			
			if (matcher.find()) {
				String join = relationshipData.join();
            	int index = join.indexOf(" ON ");
            	String cutJoin = join.substring(index + 4);
            	joinBuilder.append(" AND " + cutJoin);
			}
    	}
    	return joinBuilder.toString();
    }
}