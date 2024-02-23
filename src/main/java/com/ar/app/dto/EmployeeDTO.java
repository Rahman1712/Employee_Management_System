package com.ar.app.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDTO {
	
	private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Double salary;
    
    private DepartmentInfo department;
    
    private String address;
    private String role;
    private LocalDate joiningDate;
    private Float yearlyBonusPercentage;
    
    private EmployeeInfo reportingManager;
}