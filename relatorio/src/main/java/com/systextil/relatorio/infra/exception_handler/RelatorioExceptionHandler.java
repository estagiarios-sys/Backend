package com.systextil.relatorio.infra.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class RelatorioExceptionHandler {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	private static final String MESSAGE = "message";

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity<Map<String, String>> return400ErrorForSQLSyntaxError(SQLSyntaxErrorException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put(MESSAGE, "Não foi possível montar o SQL. Verifique os dados passados no JSON");
        errors.put("exception message", exception.getLocalizedMessage());
    	
        return ResponseEntity.badRequest().body(errors);
    }

    /** Método chamado quando algum dado não passar por alguma validação */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put(MESSAGE, error.getDefaultMessage());
            errors.put("json field", error.getField());
            errors.put("annotation", error.getCode());
        }
        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> return400ErrorForDuplicateEntry(SQLIntegrityConstraintViolationException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put(MESSAGE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(DataBaseConnectionException.class)
    public ResponseEntity<Map<String, String>> return500ErrorForDataBaseConnectionException(DataBaseConnectionException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put(MESSAGE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.internalServerError().body(errors);
    }
    
    @ExceptionHandler(IllegalDataBaseTypeException.class)
    public ResponseEntity<Map<String, String>> return500ErrorForIllegalDataBaseTypeException(IllegalDataBaseTypeException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put(MESSAGE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.internalServerError().body(errors);
    }
    
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Void> return500ErrorForHttpClientErrorException(HttpClientErrorException exception) {
    	logger.log(Level.SEVERE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.internalServerError().build();
    }
    
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Void> return500ErrorForHttpServerErrorException(HttpServerErrorException exception) {
    	logger.log(Level.SEVERE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.internalServerError().build();
    }
    
    @ExceptionHandler(UnsupportedHttpStatusException.class)
    public ResponseEntity<Void> return500ErrorForUnsupportedHttpStatusException(UnsupportedHttpStatusException exception) {
    	logger.log(Level.SEVERE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.internalServerError().build();
    }
}