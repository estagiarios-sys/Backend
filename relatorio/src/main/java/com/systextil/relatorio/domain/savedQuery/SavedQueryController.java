package com.systextil.relatorio.domain.savedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.infra.exceptionHandler.SavedQueryQueryNameIsEmptyException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class SavedQueryController {

    @Autowired
    private SavedQueryRepository savedQueryRepository;
    private ObjectMapper objectMapper;

    @GetMapping("find/saved-query")
    public List<SavedQueryListing> getSQL() {
        List<SavedQuery> queriesList = savedQueryRepository.findAll();

        return queriesList.stream()
                .map(SavedQueryListing::new)
                .toList();
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SavedQuery> saveSQL(
            @RequestParam String stringSavedQuerySaving,
            @RequestParam(required = false, value = "imgPDF") MultipartFile file
    ) throws IOException {
    	byte[] imgPDF = null;
    	
    	try {
    		imgPDF = file.getBytes();
    	} catch (NullPointerException exception) {}
    	objectMapper = new ObjectMapper();
    	SavedQuerySaving savedQuerySaving = objectMapper.readValue(stringSavedQuerySaving, SavedQuerySaving.class);
    	SavedQuery savedQuery = new SavedQuery(savedQuerySaving, imgPDF);
    	savedQueryRepository.save(savedQuery);

    	return ResponseEntity.created(null).body(savedQuery);
    }

    @DeleteMapping("delete/{queryName}")
    public ResponseEntity<Void> deleteSQL(@PathVariable String queryName) {
    	savedQueryRepository.deleteByQueryName(queryName);
    	
    	return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/saved-query", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<Void> updateSQL(
    		@RequestParam String stringSavedQuerySaving,
            @RequestParam(required = false, value = "imgPDF") MultipartFile file
    ) throws IOException, SavedQueryQueryNameIsEmptyException {
    	byte[] imgPDF = null;
    	
    	try {
    		imgPDF = file.getBytes();
    	} catch (NullPointerException exception) {}
    	objectMapper = new ObjectMapper();
    	SavedQuerySaving savedQuerySaving = objectMapper.readValue(stringSavedQuerySaving, SavedQuerySaving.class);
    	Optional<SavedQuery> optionalSavedQuery = savedQueryRepository.findByQueryName(savedQuerySaving.queryName());
        	
        if (optionalSavedQuery.isPresent()) {
        	optionalSavedQuery.get().updateData(savedQuerySaving, imgPDF);
        		
        	return ResponseEntity.ok().build();
        } else {
        	throw new SavedQueryQueryNameIsEmptyException();
        }
    }
}
