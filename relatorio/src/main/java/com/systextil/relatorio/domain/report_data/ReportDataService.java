package com.systextil.relatorio.domain.report_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.systextil.relatorio.domain.Totalizer;

class ReportDataService {
	
	private ReportDataService() {
		throw new IllegalStateException("Utility class");
	}

	static TreatedReportData treatReportData(ReportData reportData, Map<String, Totalizer> totalizers) {
    	ArrayList<String> columnsNameOrNickName = columnsNameAndNickNameToColumnsNameOrNickName(reportData.columnsNameAndNickName());
    	Map<String, String> columnsAndTotalizersResult = null;
    	
    	if (totalizers != null) {
    		columnsAndTotalizersResult = joinColumnsAndTotalizersResult(reportData, totalizers);
    	}
    	return new TreatedReportData(columnsNameOrNickName, reportData.foundObjects(), columnsAndTotalizersResult);
    }
    
    private static Map<String, String> joinColumnsAndTotalizersResult(ReportData reportData, Map<String, Totalizer> totalizers) {
    	int totalizersResultsCounter = 0;
        Map<String, String> columnsAndTotalizersResult = new HashMap<>();
        
        for (Map.Entry<String, Totalizer> totalizer : totalizers.entrySet()) {
        	String columnsAndTotalizersColumn = null;
        	
        	for (Map.Entry<String, String> columnNameAndNickName : reportData.columnsNameAndNickName().entrySet()) {
        		if (totalizer.getKey().equalsIgnoreCase(columnNameAndNickName.getKey())) {
        			if (columnNameAndNickName.getValue() != null) {
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