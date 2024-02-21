package com.ar.app.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
	
	private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Double salary;
    private DepartmentDTO department;
    private String address;
    private String role;
    private LocalDate joiningDate;
    private Float yearlyBonusPercentage;
    private EmployeeDTO reportingManager;
}