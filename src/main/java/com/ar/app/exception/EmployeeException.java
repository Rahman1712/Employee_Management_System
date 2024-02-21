package com.ar.app.exception;

import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String message;
	private HttpStatusCode status;
}
