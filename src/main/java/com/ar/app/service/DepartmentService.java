package com.ar.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ar.app.dto.DepartmentDTO;
import com.ar.app.entity.Department;
import com.ar.app.entity.Employee;
import com.ar.app.repository.DepartmentRepository;
import com.ar.app.repository.EmployeeRepository;
import com.ar.app.util.DepartmentUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    public DepartmentDTO getDepartmentById(Long id) {
    	Department department = departmentRepository.findById(id).orElse(null);
    	return DepartmentUtils.toDto(department);
    }
    
    public List<DepartmentDTO> getAllDepartments() {
    	List<DepartmentDTO> departments = departmentRepository.findAll().stream()
    			.map(DepartmentUtils::toDto)
    			.collect(Collectors.toList());
    	return departments;
    }

    public DepartmentDTO createDepartment(DepartmentDTO departmentDto) {
    	Department department = new Department();
    	
    	department.setName(departmentDto.getName());
    	department.setCreationDate(LocalDate.now());
    	if(departmentDto.getDepartmentHeadId() != null) {
    		Employee deptHead = employeeRepository.findById(departmentDto.getDepartmentHeadId())
    			.orElseThrow(() -> new RuntimeException("Employee not Found with ID: "+departmentDto.getDepartmentHeadId()));
    		department.setDepartmentHead(deptHead);
    	}
        Department saved = departmentRepository.save(department);
        
        return DepartmentUtils.toDto(saved);
    }
    
    public DepartmentDTO updateDepartmentHeadById(Long id, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));

        department.setName(departmentDTO.getName());
        department.setCreationDate(departmentDTO.getCreationDate());
        // Change the Department
        if(departmentDTO.getDepartmentHeadId() != null) {
    		Employee deptHead = employeeRepository.findById(departmentDTO.getDepartmentHeadId())
    			.orElseThrow(() -> new RuntimeException("Employee not Found with ID: "+departmentDTO.getDepartmentHeadId()));
    		department.setDepartmentHead(deptHead);
    	}

        department = departmentRepository.save(department);

        return DepartmentUtils.toDto(department);
    }

    public void deleteDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));
        
        departmentRepository.delete(department);
    }
}
