package com.systextil.relatorio.infra;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RelatorioExceptionHandler {

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity return400ErrorForSQLSyntaxError(SQLSyntaxErrorException exception) {
        String message = "Não foi possível montar o SQL. Verifique os dados passados no JSON. Segue mensagem do SQLSyntaxErrorException:";
        Object messages = new Object[]{message, exception.getLocalizedMessage()};
        return ResponseEntity.badRequest().body(messages);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
//            MethodArgumentNotValidException ex
//    ) {
//        Map<String, Object> body = new HashMap<>();
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//            errors.put(error.getField(), error.getDefaultMessage());
//        }
//
//        body.put("errors", errors);
//        body.put("code", HttpStatus.BAD_REQUEST.value());
//        return ResponseEntity.badRequest().body(body);
//    }

}
