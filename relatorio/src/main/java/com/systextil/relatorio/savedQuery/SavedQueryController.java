package com.systextil.relatorio.savedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<SavedQuery> saveSQL(@RequestBody SavedQuery savedQuery) {
        SavedQuery returnedSavedQuery = savedQueryRepository.save(savedQuery);

        return ResponseEntity.created(null).body(returnedSavedQuery);
    }

}