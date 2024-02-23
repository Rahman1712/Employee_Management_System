package com.ar.app.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentDTO {
	
	private Long id;
    private String name;
    private LocalDate creationDate;
    private EmployeeInfo departmentHead;
}