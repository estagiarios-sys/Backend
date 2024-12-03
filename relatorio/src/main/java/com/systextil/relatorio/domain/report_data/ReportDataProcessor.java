package com.systextil.relatorio.domain.report_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.systextil.relatorio.domain.Totalizer;

@Component
class ReportDataProcessor {

	Map<String, String> joinColumnsAndTotalizersResult(ReportData reportData, Map<String, Totalizer> totalizers) {
    	int totalizersResultsCounter = 0;
        Map<String, String> columnsAndTotalizersResult = new HashMap<>();
        
        for (Map.Entry<String, Totalizer> totalizer : totalizers.entrySet()) {
        	String columnsAndTotalizersColumn = null;
        	
        	for (Map.Entry<String, String> columnNameAndNickName : reportData.columnsNameAndNickName().entrySet()) {
        		if (totalizer.getKey().equalsIgnoreCase(columnNameAndNickName.getKey())) {
        			if (columnNameAndNickName.getValue() != null && !columnNameAndNickName.getValue().isBlank()) {
        				columnsAndTotalizersColumn = columnNameAndNickName.getValue();
        			} else {
        				columnsAndTotalizersColumn = columnNameAndNickName.getKey();
        			}
        		}
        	}
        	columnsAndTotalizersResult.put(columnsAndTotalizersColumn, totalizer.getValue().toPortuguese() + ": " + reportData.totalizersResult().get(totalizersResultsCounter));
        	totalizersResultsCounter++;
        }
        return columnsAndTotalizersResult;
    }

	List<String> toColumnsNameOrNickName(Map<String, String> columnsNameAndNickName) {
    	List<String> columnsNameOrNickName = new ArrayList<>();
    	
    	for (Map.Entry<String, String> columnNameAndNickName : columnsNameAndNickName.entrySet()) {
   			if (columnNameAndNickName.getValue() != null) {
   				columnsNameOrNickName.add(columnNameAndNickName.getValue());
   			} else {
   				columnsNameOrNickName.add(columnNameAndNickName.getKey());
    		}
    	}
    	return columnsNameOrNickName;
    }
}