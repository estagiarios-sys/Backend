package com.systextil.relatorio.domain.saved_query;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
public class SavedQueryController {

    private final SavedQueryRepository repository;
    private ObjectMapper objectMapper;
    
    public SavedQueryController(SavedQueryRepository savedQueryRepository) {
    	this.repository = savedQueryRepository;
    }

    @GetMapping("list/saved-query")
    public List<AllSavedQueriesListing> getSQL() {
        return repository.findAllForListing();
    }
    
    @GetMapping("get/saved-query/{id}")
    public OneSavedQueryListing getSavedQueryById(@PathVariable Long id) {
    	return new OneSavedQueryListing(repository.getReferenceById(id));
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SavedQuery> saveQuery(
            @RequestParam String stringSavedQuerySaving,
            @RequestParam(required = false, value = "imgPDF") MultipartFile file
    ) throws IOException {
    	byte[] imgPDF;
    	
    	try {
    		imgPDF = file.getBytes();
    	} catch (NullPointerException exception) {
    		imgPDF = null;
    	}
    	objectMapper = new ObjectMapper();
    	SavedQuerySaving savedQuerySaving = objectMapper.readValue(stringSavedQuerySaving, SavedQuerySaving.class);
    	SavedQuery savedQuery = new SavedQuery(savedQuerySaving, imgPDF);
    	repository.save(savedQuery);

    	return ResponseEntity.created(URI.create("")).body(savedQuery);
    }

    @PutMapping(value = "/update/saved-query/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<Void> updateQuery(
    		@RequestParam String stringSavedQueryUpdating,
            @RequestParam(required = false, value = "imgPDF") MultipartFile file,
            @PathVariable Long id
    ) throws IOException {
    	byte[] imgPDF;
    	
    	try {
    		imgPDF = file.getBytes();
    	} catch (NullPointerException exception) {
    		imgPDF = null;
    	}
    	objectMapper = new ObjectMapper();
    	SavedQueryUpdating updating = objectMapper.readValue(stringSavedQueryUpdating, SavedQueryUpdating.class);
    	SavedQuery savedQuery = repository.getReferenceById(id);
        	
        savedQuery.updateData(updating, imgPDF);
        		
        return ResponseEntity.ok().build();
        
    }
    
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Long id) {
    	repository.deleteById(id);
    	
    	return ResponseEntity.noContent().build();
    }
}