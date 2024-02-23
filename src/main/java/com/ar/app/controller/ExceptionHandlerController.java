package com.ar.app.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ar.app.exception.DepartmentException;
import com.ar.app.exception.EmployeeException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	@ExceptionHandler(EmployeeException.class)
    public ResponseEntity<String> handleEmployeeException(EmployeeException ex) {
        log.error("EmployeeException : {}", ex.getMessage());
        return ResponseEntity
        		.status(ex.getStatus())
        		.body(ex.getMessage());
    }

	@ExceptionHandler(DepartmentException.class)
    public ResponseEntity<String> handleDepartmentException(DepartmentException ex) {
        log.error("DepartmentException : {}", ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), ex.getStatus()); 
    }
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
         Map<String, String> collect = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
         log.error("MethodArgumentNotValidException : {}", ex.getMessage());
         return new ResponseEntity<Map<String,String>>(collect, HttpStatus.BAD_REQUEST); 
    }
	
}
