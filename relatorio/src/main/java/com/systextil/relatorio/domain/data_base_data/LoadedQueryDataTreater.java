package com.systextil.relatorio.domain.data_base_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.systextil.relatorio.domain.ColumnAndTotalizer;
import com.systextil.relatorio.domain.TotalizerTypes;

class LoadedQueryDataTreater {
	
	private LoadedQueryDataTreater() {
		throw new IllegalStateException("Utility class");
	}

	static TreatedLoadedQueryData treatLoadedQueryData(LoadedQueryData loadedQueryData, List<ColumnAndTotalizer> totalizers) {
    	ArrayList<String> columnsNameOrNickName = columnsNameAndNickNameToColumnsNameOrNickName(loadedQueryData.columnsNameAndNickName());
    	Map<String, String> columnsAndTotalizersResult = null;
    	
    	if (totalizers != null) {
    		columnsAndTotalizersResult = joinColumnsAndTotalizersResult(loadedQueryData, totalizers);
    	}
    	
    	return new TreatedLoadedQueryData(columnsNameOrNickName, loadedQueryData.foundObjects(), columnsAndTotalizersResult);
    }
    
    private static Map<String, String> joinColumnsAndTotalizersResult(LoadedQueryData loadedQueryData, List<ColumnAndTotalizer> totalizers) {
    	int totalizersResultsCounter = 0;
        Map<String, String> columnsAndTotalizersResult = new HashMap<>();
        
        for (ColumnAndTotalizer columnAndTotalizer : totalizers) {
        	Entry<String, TotalizerTypes> totalizerAndColumn = columnAndTotalizer.totalizer().entrySet().iterator().next();
        	String columnsAndTotalizersColumn = null;
        	
        	for (Map.Entry<String, String> columnNameAndNickName : loadedQueryData.columnsNameAndNickName().entrySet()) {
        		
        		if (totalizerAndColumn.getKey().equalsIgnoreCase(columnNameAndNickName.getKey())) {
        			if (columnNameAndNickName.getValue() != null) {
        				columnsAndTotalizersColumn = columnNameAndNickName.getValue();
        			} else {
        				columnsAndTotalizersColumn = columnNameAndNickName.getKey();
        			}
        		}
        	}
        	columnsAndTotalizersResult.put(columnsAndTotalizersColumn, totalizerAndColumn.getValue().toPortuguese() + ": " + loadedQueryData.totalizersResult().get(totalizersResultsCounter));
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