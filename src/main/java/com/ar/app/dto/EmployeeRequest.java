package com.ar.app.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeRequest {
    
	@NotNull
	@Size(min = 2, max = 50, message = "Name must be at least 2 characters long")
	private String name;
	
	@NotNull(message = "Date of Birth is required in 'yyyy-MM-dd' format")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
	
	@Positive(message = "Salary must be a positive number")
    private Double salary;
    private Long departmentId;
    private String address;
    
    @NotBlank(message = "Role is required")
    @Size(min = 2, message = "Role must be at least 2 characters long")
    private String role;
    
    @NotNull(message = "Joining date is required in 'yyyy-MM-dd' format")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;
    
    @Positive(message = "Yearly bonus percentage must be a positive number")
    private Float yearlyBonusPercentage;
    
    private Long reportingManagerId;
}

