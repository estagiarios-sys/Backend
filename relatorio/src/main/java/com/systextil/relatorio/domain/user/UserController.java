package com.systextil.relatorio.domain.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.sql.SQLException;
import java.util.List;

@RestController
public class UserController {

    private final UserRepository repository;
    private final UserService service;
    
    public UserController(UserRepository repository, UserService service) {
        this.repository = repository;
        this.service = service;
    }
    
    @GetMapping("companies")
    public List<Company> getCompanies() throws SQLException {
    	return repository.getCompanies();
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) throws SQLException {
        return service.login(loginRequest);
    }
}