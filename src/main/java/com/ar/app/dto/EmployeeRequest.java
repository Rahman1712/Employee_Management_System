package com.ar.app.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    
	private String name;
    private LocalDate dateOfBirth;
    private Double salary;
    private Long departmentId;
    private String address;
    private String role;
    private LocalDate joiningDate;
    private Float yearlyBonusPercentage;
    private Long reportingManagerId;
}
