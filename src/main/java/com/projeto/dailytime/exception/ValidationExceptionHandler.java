package com.projeto.dailytime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro -> erros.put(erro.getField(), erro.getDefaultMessage()));

        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
    }

}
