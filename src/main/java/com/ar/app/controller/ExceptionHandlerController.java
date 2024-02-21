package com.ar.app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ar.app.exception.DepartmentException;
import com.ar.app.exception.EmployeeException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController extends ResponseEntityExceptionHandler{

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
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		return new ResponseEntity<Object>("Method "+ex.getMethod()+" Not Allowed : Please change method type.", HttpStatus.METHOD_NOT_ALLOWED); 
	}
	
}
