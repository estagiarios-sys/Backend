package com.systextil.relatorio.domain.report_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.systextil.relatorio.domain.Totalizer;

class ReportDataTreater {
	
	private ReportDataTreater() {
		throw new IllegalStateException("Utility class");
	}

	static TreatedReportData treatLoadedQueryData(ReportData loadedQueryData, Map<String, Totalizer> totalizers) {
    	ArrayList<String> columnsNameOrNickName = columnsNameAndNickNameToColumnsNameOrNickName(loadedQueryData.columnsNameAndNickName());
    	Map<String, String> columnsAndTotalizersResult = null;
    	
    	if (totalizers != null) {
    		columnsAndTotalizersResult = joinColumnsAndTotalizersResult(loadedQueryData, totalizers);
    	}
    	
    	return new TreatedReportData(columnsNameOrNickName, loadedQueryData.foundObjects(), columnsAndTotalizersResult);
    }
    
    private static Map<String, String> joinColumnsAndTotalizersResult(ReportData loadedQueryData, Map<String, Totalizer> totalizers) {
    	int totalizersResultsCounter = 0;
        Map<String, String> columnsAndTotalizersResult = new HashMap<>();
        
        for (Map.Entry<String, Totalizer> totalizer : totalizers.entrySet()) {
        	String columnsAndTotalizersColumn = null;
        	
        	for (Map.Entry<String, String> columnNameAndNickName : loadedQueryData.columnsNameAndNickName().entrySet()) {
        		
        		if (totalizer.getKey().equalsIgnoreCase(columnNameAndNickName.getKey())) {
        			if (columnNameAndNickName.getValue() != null) {
        				columnsAndTotalizersColumn = columnNameAndNickName.getValue();
        			} else {
        				columnsAndTotalizersColumn = columnNameAndNickName.getKey();
        			}
        		}
        	}
        	columnsAndTotalizersResult.put(columnsAndTotalizersColumn, totalizer.getValue().toPortuguese() + ": " + loadedQueryData.totalizersResult().get(totalizersResultsCounter));
        	totalizersResultsCounter++;
        }
        
        return columnsAndTotalizersResult;
    }
    
    private static ArrayList<String> columnsNameAndNickNameToColumnsNameOrNickName(Map<String, String> columnsNameAndNickName) {
    	ArrayList<String> columnsNameOrNickName = new ArrayList<>();
    	
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