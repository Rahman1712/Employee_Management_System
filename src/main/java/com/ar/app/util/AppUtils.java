package com.ar.app.util;

import com.ar.app.dto.DepartmentDTO;
import com.ar.app.dto.EmployeeDTO;
import com.ar.app.entity.Department;
import com.ar.app.entity.Employee;

public class AppUtils {
	
	public static DepartmentDTO DepartmentToDto(Department department) {
        DepartmentDTO departmentDTO = DepartmentDTO.builder()
	        .id(department.getId())
	        .name(department.getName())
	        .creationDate(department.getCreationDate())
	        .build();
        
        if(department.getDepartmentHead() != null) {
        	EmployeeDTO departmentHead = EmployeeDTO.builder()
            		.id(department.getDepartmentHead().getId())
            		.name(department.getDepartmentHead().getName())
            		.build();
        	departmentDTO.setDepartmentHead(departmentHead);
        }

        return departmentDTO;
    }
	
	public static EmployeeDTO EmployeeToDto(Employee employee) {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
        		.id(employee.getId())
        		.name(employee.getName())
        		.dateOfBirth(employee.getDateOfBirth())
    			.salary(employee.getSalary())
    			.address(employee.getAddress())
    			.role(employee.getRole())
    			.joiningDate(employee.getJoiningDate())
    			.yearlyBonusPercentage(employee.getYearlyBonusPercentage())
        		.build();
        if(employee.getDepartment() != null ) {
        	DepartmentDTO department = DepartmentDTO.builder()
        		.id(employee.getDepartment().getId())
        		.name(employee.getDepartment().getName())
        		.build();
        	employeeDTO.setDepartment(department);
        }
        
        if(employee.getReportingManager() != null ) {
        	EmployeeDTO reportingManager = EmployeeDTO.builder()
        		.id(employee.getReportingManager().getId())
        		.name(employee.getReportingManager().getName())
        		.build();
        	employeeDTO.setReportingManager(reportingManager);
        }
        
        return employeeDTO;
    }
}
