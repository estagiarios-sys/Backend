package com.systextil.relatorio.domain.relationship;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.systextil.relatorio.domain.RelationshipData;

@Component
class MultiJoinReducer {

    List<String> findDuplicates(List<String> tablesPairs) {
        Map<String, Long> counts = tablesPairs.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        return counts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
    }
    
    List<RelationshipData> cutDuplicates(List<String> duplicates, List<RelationshipData> relationships) {        
        for (String duplicate : duplicates) {
            List<RelationshipData> foundRelationships = relationships.stream()
                .filter(relat -> relat.tablesPair().equals(duplicate))
                .toList();
            StringBuilder joinBuilder = new StringBuilder();
            boolean isFirst = true;
                
            for (RelationshipData relationshipData : foundRelationships) {
            	if (!isFirst) {
            		String join = relationshipData.join();
                	int index = join.indexOf(" ON ");
                	String cutJoin = join.substring(index + 4);
                	joinBuilder.append(" AND " + cutJoin);
            	} else {
            		joinBuilder.append(relationshipData.join());
                	isFirst = false;
            	}
            }
            relationships.removeIf(relat -> relat.tablesPair().equals(duplicate));
            RelationshipData newRelationship = new RelationshipData(duplicate, joinBuilder.toString());
            relationships.add(newRelationship);
        }
        return relationships;
    }
}