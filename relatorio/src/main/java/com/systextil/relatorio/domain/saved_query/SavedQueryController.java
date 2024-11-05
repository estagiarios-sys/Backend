package com.systextil.relatorio.domain.saved_query;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("saved-query")
public class SavedQueryController {

	private final SavedQueryService service;
    private final SavedQueryRepository repository;
    
    public SavedQueryController(SavedQueryService service, SavedQueryRepository savedQueryRepository) {
    	this.service = service;
    	this.repository = savedQueryRepository;
    }

    @GetMapping
    public List<AllSavedQueriesListing> getSQL() {
        return repository.findAllForListing();
    }
    
    @GetMapping("{id}")
    public OneSavedQueryListing getSavedQueryById(@PathVariable Long id) {
    	return new OneSavedQueryListing(repository.getReferenceById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SavedQuery> saveQuery(
            @RequestParam String stringSavedQuerySaving,
            @RequestParam(required = false, value = "imgPDF") MultipartFile file
    ) throws IOException {
    	SavedQuery savedQuery = service.saveQuery(stringSavedQuerySaving, file);

    	return ResponseEntity.created(URI.create("")).body(savedQuery);
    }

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<Void> updateQuery(
    		@RequestParam String stringSavedQueryUpdating,
            @RequestParam(required = false, value = "imgPDF") MultipartFile file,
            @PathVariable Long id
    ) throws IOException {
    	service.updateQuery(stringSavedQueryUpdating, file, id);
        		
        return ResponseEntity.ok().build();
        
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Long id) {
    	repository.deleteById(id);
    	
    	return ResponseEntity.noContent().build();
    }
}