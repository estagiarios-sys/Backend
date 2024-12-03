package com.systextil.relatorio.domain.report_data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.systextil.relatorio.domain.RelationshipData;

@Component
class QueryDataPreparer {
	
	private final ReportDataStorageAccessor storageAccessor;
	
	QueryDataPreparer(ReportDataStorageAccessor storageAccessor) {
		this.storageAccessor = storageAccessor;
	}

	List<String> findJoinsByTablesPairs(List<String> tablesPairs) throws IOException {
		List<String> joins = new ArrayList<>();
    	List<RelationshipData> relationshipData = storageAccessor.findRelationshipData();
    	
    	for (String tablesPair : tablesPairs) {
    		for (RelationshipData tablesPairAndJoin : relationshipData) {
    			if (tablesPair.equals(tablesPairAndJoin.tablesPair())) {
    				joins.add(tablesPairAndJoin.join());
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
}