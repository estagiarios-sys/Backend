package com.systextil.relatorio.domain.table;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("tables")
public class TableController {
	
    private final TableService service;
    
    public TableController(TableService service) {
    	this.service = service;
    }
    
	@GetMapping
    public ResponseEntity<Resource> getTables() throws IOException {
    	return ResponseEntity.ok(service.getTables());
    }
	
	@PostMapping("columns")
	public ResponseEntity<Map<String, Map<String, String>>> getColumnsFromTables(@RequestBody @Valid AllTables allTables) throws SQLException {
		return ResponseEntity.ok(service.getColumnsFromTables(allTables));
	}

    @PutMapping
    public ResponseEntity<Void> setTablesIntoJson() throws IOException, SQLException {
    	service.setTablesIntoJson();
    	
    	return ResponseEntity.ok(null);
    }
}