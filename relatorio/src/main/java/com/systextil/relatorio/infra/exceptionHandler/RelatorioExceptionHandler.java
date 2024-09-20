package com.systextil.relatorio.infra.exceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RelatorioExceptionHandler {

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity<Map<String, String>> return400ErrorForSQLSyntaxError(SQLSyntaxErrorException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put("message", "Não foi possível montar o SQL. Verifique os dados passados no JSON");
        errors.put("exception message", exception.getLocalizedMessage());
    	
        return ResponseEntity.badRequest().body(errors);
    }

    /** Método chamado quando algum dado não passar por alguma validação */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put("message", error.getDefaultMessage());
            errors.put("json field", error.getField());
            errors.put("annotation", error.getCode());
        }
        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> return400ErrorForDuplicateEntry(SQLIntegrityConstraintViolationException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put("message", exception.getLocalizedMessage());
    	
    	return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(TotalizerFieldsMismatchException.class)
    public ResponseEntity<Map<String, String>> return400ErrorForTotalizerFieldsMismatchException(TotalizerFieldsMismatchException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put("message", exception.getLocalizedMessage());
    	
    	return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CannotConnectToDataBaseException.class)
    public ResponseEntity<Map<String, String>> return500ErrorForCannotConnectToDataBaseException(CannotConnectToDataBaseException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put("message", exception.getLocalizedMessage());
    	
    	return ResponseEntity.internalServerError().body(errors);
    }
    
    @ExceptionHandler(ActualTimeNotFoundException.class)
    public ResponseEntity<Map<String, String>> return500ErrorForActualTimeNotFoundException(ActualTimeNotFoundException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put("message", exception.getLocalizedMessage());
    	
    	return ResponseEntity.internalServerError().body(errors);
    }
}