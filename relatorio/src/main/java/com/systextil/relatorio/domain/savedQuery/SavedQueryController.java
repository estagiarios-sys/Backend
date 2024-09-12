package com.systextil.relatorio.domain.savedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
public class SavedQueryController {

    @Autowired
    private SavedQueryRepository savedQueryRepository;

    @GetMapping("find/saved-query")
    public List<SavedQueryListing> getSQL() {
        List<SavedQuery> queriesList = savedQueryRepository.findAll();

        return queriesList.stream()
                .map(SavedQueryListing::new)
                .toList();
    }

    @PostMapping("save")
    public ResponseEntity<SavedQuery> saveSQL(@RequestBody @Valid SavedQuerySaving savedQuerySaving) {
    	SavedQuery savedQuery = new SavedQuery(savedQuerySaving);
        savedQueryRepository.save(savedQuery);

        return ResponseEntity.created(null).body(savedQuery);
    }
    
    @DeleteMapping("delete/{queryName}")
    public ResponseEntity<Void> deleteSQL(@PathVariable(value = "queryName") String queryName) {
    	savedQueryRepository.deleteByQueryName(queryName);
    	
    	return ResponseEntity.noContent().build();
    }

    @PutMapping("update/saved-query")
    @Transactional
    public ResponseEntity<Void> updateSQL(@RequestBody @Valid SavedQuerySaving savedQuerySaving) {
    	Optional<SavedQuery> optionalSavedQuery = savedQueryRepository.findByQueryName(savedQuerySaving.queryName());
    	
    	if (optionalSavedQuery.isPresent()) {
    		optionalSavedQuery.get().updateData(savedQuerySaving);
    		
    		return ResponseEntity.ok().build();
    	} else {
    		
    		return ResponseEntity.badRequest().build();
    	}
    }
}
