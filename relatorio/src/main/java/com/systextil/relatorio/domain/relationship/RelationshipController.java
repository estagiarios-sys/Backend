package com.systextil.relatorio.domain.relationship;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("relationships")
public class RelationshipController {
	
	private final RelationshipService service;
	
	public RelationshipController(RelationshipService service) {
		this.service = service;
	}

	@GetMapping
    public ResponseEntity<Resource> getRelationships() throws IOException {
        return ResponseEntity.ok(service.getRelationships());
    }
	
	@PutMapping
    public ResponseEntity<Void> setRelationshipsIntoJson() throws SQLException, IOException {
        service.setRelationshipsIntoJson();
        
        return ResponseEntity.ok(null);
    }
}