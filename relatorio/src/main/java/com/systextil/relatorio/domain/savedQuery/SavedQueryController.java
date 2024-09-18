package com.systextil.relatorio.domain.savedQuery;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SavedQuery> saveSQL(
            @RequestParam("queryData") String queryData,  // Aqui você recebe os dados JSON como String
            @RequestParam("imgPDF") MultipartFile file  // Aqui você recebe a imagem como arquivo
    ) {
        try {
            // Converter a string JSON para um objeto Java
            ObjectMapper objectMapper = new ObjectMapper();
            SavedQuerySaving savedQuerySaving = objectMapper.readValue(queryData, SavedQuerySaving.class);  // Converte o JSON para objeto

            // Processar a imagem
            byte[] imgPDF = file.getBytes();  // Converte o MultipartFile para byte[] para salvar no banco

            // Criar a entidade usando os dados do JSON e a imagem
            SavedQuery savedQuery = new SavedQuery(savedQuerySaving);
            savedQuery.setImgPDF(imgPDF);  // Adiciona a imagem ao objeto

            savedQueryRepository.save(savedQuery);  // Salva no banco de dados

            return ResponseEntity.created(null).body(savedQuery);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }




    @DeleteMapping("delete/{queryName}")
    public ResponseEntity<Void> deleteSQL(@PathVariable String queryName) {
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
