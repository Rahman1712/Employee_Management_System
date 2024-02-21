package com.ar.app.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ar.app.dto.DepartmentDTO;
import com.ar.app.dto.DepartmentRequest;
import com.ar.app.entity.Department;
import com.ar.app.entity.Employee;
import com.ar.app.exception.DepartmentException;
import com.ar.app.exception.EmployeeException;
import com.ar.app.repository.DepartmentRepository;
import com.ar.app.repository.EmployeeRepository;
import com.ar.app.util.AppUtils;

@Service
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    private static final int PAGE_LIMIT = 20;
    
    public DepartmentDTO getDepartmentById(Long id) {
    	Department department = departmentRepository.findById(id)
    			.orElseThrow(() -> new DepartmentException(
                		"Department not Found with ID: " + id,
                		HttpStatus.NOT_FOUND));
    	
    	return AppUtils.DepartmentToDto(department);
    }
    
    public List<DepartmentDTO> getAllDepartments() {
    	List<DepartmentDTO> departments = departmentRepository.findAll().stream()
    			.map(AppUtils::DepartmentToDto)
    			.collect(Collectors.toList());
    	
    	return departments;
    }
    
	public Map<String, Object> getDepartmentsByPage(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_LIMIT); 
		Page<Department> page = departmentRepository.findAll(pageable);
		
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		List<DepartmentDTO> departments = page.getContent().stream()
				.map(AppUtils::DepartmentToDto).collect(Collectors.toList());

		Map<String, Object> map = new HashMap<>();
		map.put("pageNum", pageNum);
		map.put("totalItems", totalItems);
		map.put("totalPages", totalPages);
		map.put("departments", departments);
		
		return map;
	}
	
    public DepartmentDTO createDepartment(DepartmentRequest departmentRequest) {
        
        if (departmentRepository.existsByName(departmentRequest.getName())) {
        	throw new DepartmentException(
        			"Department with name '" + departmentRequest.getName() + "' already exists", 
        			HttpStatus.BAD_REQUEST);
        }
        
        Department department = new Department();
        
        department.setName(departmentRequest.getName());
        department.setCreationDate(LocalDate.now());
        if(departmentRequest.getDepartmentHeadId() != null) {
            Employee deptHead = employeeRepository.findById(departmentRequest.getDepartmentHeadId())
                .orElseThrow(() -> new EmployeeException(
                		"Employee not Found with ID: " + departmentRequest.getDepartmentHeadId(),
                		HttpStatus.NOT_FOUND));
            
            department.setDepartmentHead(deptHead);
        }
        Department saved = departmentRepository.save(department);
        
        return AppUtils.DepartmentToDto(saved);
    }
    
    public DepartmentDTO updateDepartmentById(Long id, DepartmentRequest departmentRequest) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentException("Department not found with ID: " + id , HttpStatus.NOT_FOUND));
        
        if (departmentRepository.existsByName(departmentRequest.getName())) {
            throw new DepartmentException(
            		"Department with name '" + departmentRequest.getName() + "' already exists", 
            		HttpStatus.BAD_REQUEST);
        }

        department.setName(departmentRequest.getName());
        department.setCreationDate(departmentRequest.getCreationDate());
        
        // Change the Department
        if(departmentRequest.getDepartmentHeadId() != null) {
    		Employee deptHead = employeeRepository.findById(departmentRequest.getDepartmentHeadId())
    			.orElseThrow(() -> new EmployeeException("Employee not Found with ID: "+departmentRequest.getDepartmentHeadId(),HttpStatus.NOT_FOUND));
    		
    		department.setDepartmentHead(deptHead);
    	}

        department = departmentRepository.save(department);

        return AppUtils.DepartmentToDto(department);
    }

    public void deleteDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentException("Department not found with ID: " + id , HttpStatus.NOT_FOUND));
        
        departmentRepository.delete(department);
    }


}
