package com.systextil.relatorio.domain.relationship;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.systextil.relatorio.domain.RelationshipData;

@Component
public class FindDuplicates {

    public List<String> findDuplicates(List<String> tablesPairs) {
        Map<String, Long> counts = tablesPairs.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        return counts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
    }
    
    public List<RelationshipData> cutDuplicates(List<String> duplicates, List<RelationshipData> relationships) {
    	for (String duplicate : duplicates) {
    		List<RelationshipData> foundRelat = relationships.stream().
    			filter(relat -> relat.tablesPair().equals(duplicate)).
    			toList();
    		
    		boolean isFirst = true;
    		
    		for (RelationshipData relationshipData : foundRelat) {
    			if (!isFirst) {
    				String join = relationshipData.join();
    				
    				int indice = join.indexOf(" ON ");
    				
    				String cutJoin = join.substring(indice + 4);
    				
    				String joinWithAnd = " AND " + cutJoin;
    				
    				RelationshipData newRelation = new RelationshipData(relationshipData.tablesPair(), joinWithAnd);
    					
    			}
    			isFirst = false;
    		}
    		
    		System.out.println("Testando: " + relationships);
    		
    	}
    	
    	return null;
    }
}