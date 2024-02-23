package com.ar.app.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentRequest {

	@NotNull
	@Size(min = 2, max = 25, message = "Name must be at least 2 characters long")
	private String name;
	
	@NotNull(message = "Creation Date is required in 'yyyy-MM-dd' format")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;
	
    private Long departmentHeadId;
}
