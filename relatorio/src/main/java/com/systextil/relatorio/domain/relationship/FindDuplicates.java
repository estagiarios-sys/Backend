package com.systextil.relatorio.domain.relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
       
        List<RelationshipData> updatedRelationships = new ArrayList<>(relationships);
        
        for (String duplicate : duplicates) {
           
            List<RelationshipData> foundRelat = relationships.stream()
                .filter(relat -> relat.tablesPair().equals(duplicate))
                .collect(Collectors.toList());
            
            if (foundRelat.size() > 1) {
             
                RelationshipData firstRelat = foundRelat.get(0);
                String combinedJoin = firstRelat.join();
                
                
                for (int i = 1; i < foundRelat.size(); i++) {
                    RelationshipData relationshipData = foundRelat.get(i);
                    String join = relationshipData.join();
                    
                    int indice = join.indexOf(" ON ");
                    if (indice != -1) {
                        
                        String cutJoin = join.substring(indice + 4);
                
                        combinedJoin += " AND " + cutJoin;
                    }
                }
                
            
                RelationshipData newRelation = new RelationshipData(duplicate, combinedJoin);
                
                
                updatedRelationships.removeIf(relat -> relat.tablesPair().equals(duplicate));
                
              
                updatedRelationships.add(newRelation);
            }
        }

        System.out.println("New query: " + updatedRelationships);
        
        return updatedRelationships;
    }
}
