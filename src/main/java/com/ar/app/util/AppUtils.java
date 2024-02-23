package com.ar.app.util;

import com.ar.app.dto.DepartmentDTO;
import com.ar.app.dto.DepartmentInfo;
import com.ar.app.dto.EmployeeDTO;
import com.ar.app.dto.EmployeeInfo;
import com.ar.app.entity.Department;
import com.ar.app.entity.Employee;

public class AppUtils {
	
	public static DepartmentDTO departmentToDto(Department department) {
        DepartmentDTO departmentDTO = DepartmentDTO.builder()
	        .id(department.getId())
	        .name(department.getName())
	        .creationDate(department.getCreationDate())
	        .build();
        
        if(department.getDepartmentHead() != null) {
        	EmployeeInfo departmentHead = EmployeeInfo.builder()
            		.id(department.getDepartmentHead().getId())
            		.name(department.getDepartmentHead().getName())
            		.build();
        	departmentDTO.setDepartmentHead(departmentHead);
        }

        return departmentDTO;
    }
	
	public static EmployeeDTO employeeToDto(Employee employee) {
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
        	DepartmentInfo department = DepartmentInfo.builder()
        		.id(employee.getDepartment().getId())
        		.name(employee.getDepartment().getName())
        		.build();
        	employeeDTO.setDepartment(department);
        }
        
        if(employee.getReportingManager() != null ) {
        	EmployeeInfo reportingManager = EmployeeInfo.builder()
        		.id(employee.getReportingManager().getId())
        		.name(employee.getReportingManager().getName())
        		.build();
        	employeeDTO.setReportingManager(reportingManager);
        }
        
        return employeeDTO;
    }
	
	public static EmployeeInfo employeeToInfo(Employee employee) {
		return EmployeeInfo.builder()
						   .id(employee.getId())
						   .name(employee.getName())
						   .build();
	}
	
}
