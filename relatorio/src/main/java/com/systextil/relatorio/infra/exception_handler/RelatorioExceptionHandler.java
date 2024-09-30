package com.systextil.relatorio.infra.exception_handler;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RelatorioExceptionHandler {
	
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
    	
    	return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(TotalizerFieldsMismatchException.class)
    public ResponseEntity<Map<String, String>> return400ErrorForTotalizerFieldsMismatchException(TotalizerFieldsMismatchException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put(MESSAGE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CannotConnectToDataBaseException.class)
    public ResponseEntity<Map<String, String>> return500ErrorForCannotConnectToDataBaseException(CannotConnectToDataBaseException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put(MESSAGE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.internalServerError().body(errors);
    }
    
    @ExceptionHandler(ActualTimeNotFoundException.class)
    public ResponseEntity<Map<String, String>> return500ErrorForActualTimeNotFoundException(ActualTimeNotFoundException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put(MESSAGE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.internalServerError().body(errors);
    }
    
    @ExceptionHandler(SavedQueryQueryNameIsEmptyException.class)
    public ResponseEntity<Map<String, String>> return400ErrorForSavedQueryQueryNameIsEmptyException(SavedQueryQueryNameIsEmptyException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put(MESSAGE, exception.getLocalizedMessage());
    	
    	return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> return500ErrorForHttpClientErrorException(HttpClientErrorException exception) {
    	Map<String, String> errors = new LinkedHashMap<>();
    	errors.put("internal status", exception.getStatusText());
    	
    	return ResponseEntity.internalServerError().body(errors);
    }
}