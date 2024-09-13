package com.systextil.relatorio.infra;

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
    public ResponseEntity<String[]> return400ErrorForSQLSyntaxError(SQLSyntaxErrorException exception) {
        String message = "Não foi possível montar o SQL. Verifique os dados passados no JSON. Segue mensagem do SQLSyntaxErrorException";
        String[] messages = new String[]{message, exception.getLocalizedMessage()};
        return ResponseEntity.badRequest().body(messages);
    }

    /** Método chamado quando algum dado não passar por alguma validação */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put("Mensagem:", error.getDefaultMessage());
            errors.put("Campo do JSON:", error.getField());
            errors.put("Anotação:", error.getCode());
        }
        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> return400ErrorForDuplicateEntry(SQLIntegrityConstraintViolationException exception) {
    	Map<String, String> errors = new HashMap<>();
    	errors.put("message", exception.getLocalizedMessage());
    	
    	return ResponseEntity.badRequest().body(errors);
    	
    }

}
