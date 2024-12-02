package com.systextil.relatorio.domain.saved_query;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
class SavedQueryService {
	
	private final SavedQueryRepository repository;
    private final ObjectMapper objectMapper;

	SavedQueryService(SavedQueryRepository repository, ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}
	
	SavedQuery saveQuery(String stringSavedQuerySaving, MultipartFile pdfImageFile) throws IOException {
		byte[] pdfImage = null;
		
    	if (pdfImageFile != null && !pdfImageFile.isEmpty()) {
    		pdfImage = pdfImageFile.getBytes();
    	}
    	SavedQuerySaving savedQuerySaving = objectMapper.readValue(stringSavedQuerySaving, SavedQuerySaving.class);
    	SavedQuery savedQuery = new SavedQuery(savedQuerySaving, pdfImage);
    	repository.save(savedQuery);
    	
    	return savedQuery;
	}

	void updateQuery(String stringSavedQueryUpdating, MultipartFile pdfImageFile, Long id) throws IOException {
		byte[] pdfImage = null;
		
    	if (pdfImageFile != null && !pdfImageFile.isEmpty()) {
    		pdfImage = pdfImageFile.getBytes();
    	}
    	SavedQueryUpdating updating = objectMapper.readValue(stringSavedQueryUpdating, SavedQueryUpdating.class);
    	SavedQuery savedQuery = repository.getReferenceById(id);
        	
        savedQuery.updateData(updating, pdfImage);
	}
}