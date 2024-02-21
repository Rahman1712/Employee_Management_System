package com.ar.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ar.app.dto.EmployeeDTO;
import com.ar.app.dto.EmployeeRequest;
import com.ar.app.entity.Department;
import com.ar.app.entity.Employee;
import com.ar.app.exception.DepartmentException;
import com.ar.app.exception.EmployeeException;
import com.ar.app.repository.DepartmentRepository;
import com.ar.app.repository.EmployeeRepository;
import com.ar.app.util.AppUtils;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    private static final int PAGE_LIMIT = 20;

    public EmployeeDTO createEmployee(EmployeeRequest request) {
    	Employee employee = Employee.builder()
    			.name(request.getName())
    			.dateOfBirth(request.getDateOfBirth())
    			.salary(request.getSalary())
    			.address(request.getAddress())
    			.role(request.getRole())
    			.joiningDate(request.getJoiningDate())
    			.yearlyBonusPercentage(request.getYearlyBonusPercentage())
    			.build();
    	
    	if(request.getReportingManagerId() != null) {
    		Employee reportingManager = employeeRepository
    				.findById(request.getReportingManagerId())
    			.orElseThrow(() -> 
    			new EmployeeException("Reporting Manager not Found with ID: "+request.getReportingManagerId()
    			,HttpStatus.NOT_FOUND));
    		
    		employee.setReportingManager(reportingManager);
    	}

    	if(request.getDepartmentId() != null) {
    		Department department = departmentRepository.findById(request.getDepartmentId())
    				.orElseThrow(() -> new DepartmentException("Department not found with ID: " + request.getDepartmentId(),
    						HttpStatus.NOT_FOUND));
    		
    		employee.setDepartment(department);
    	}
    	
        Employee savedEmployee = employeeRepository.save(employee);
        
        return AppUtils.EmployeeToDto(savedEmployee);
    }
    
    
    public EmployeeDTO getEmployeeById(Long id) {
    	Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeException(
                		"Employee not Found with ID: " + id,
                		HttpStatus.NOT_FOUND));
    	
    	return AppUtils.EmployeeToDto(employee);
    }
    
    
    public List<EmployeeDTO> getAllEmployees() {
    	List<EmployeeDTO> employeesList = employeeRepository.findAll().stream()
    			.map(AppUtils::EmployeeToDto)
    			.collect(Collectors.toList());
    	return employeesList;
    }

    
    public Map<String, Object> getEmployeesByPage(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_LIMIT); 
		Page<Employee> page = employeeRepository.findAll(pageable);
		
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		List<EmployeeDTO> employees = page.getContent().stream()
				.map(AppUtils::EmployeeToDto).collect(Collectors.toList());

		Map<String, Object> map = new HashMap<>();
		map.put("pageNum", pageNum);
		map.put("totalItems", totalItems);
		map.put("totalPages", totalPages);
		map.put("employees", employees);
		
		return map;
	}
    
    
    public EmployeeDTO updateEmployeeById(Long id, EmployeeRequest request) {
    	Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + id , HttpStatus.NOT_FOUND));

    	employee.setName(request.getName());
    	employee.setDateOfBirth(request.getDateOfBirth());
    	employee.setSalary(request.getSalary());
    	employee.setAddress(request.getAddress());
    	employee.setRole(request.getRole());
    	employee.setJoiningDate(request.getJoiningDate());
    	employee.setYearlyBonusPercentage(request.getYearlyBonusPercentage());
    	
    	Employee reportingManager = null;
    	if(request.getReportingManagerId() != null) {
    		reportingManager = employeeRepository.findById(request.getReportingManagerId())
    			.orElseThrow(() -> new EmployeeException("Reporting Manager not Found with ID: "+request.getReportingManagerId(),
    			HttpStatus.NOT_FOUND));

    		if(reportingManager.getId().equals(employee.getId())) {
    			throw new EmployeeException(
    					"Employee and Reporting Manager not be same person",
    	    			HttpStatus.NOT_FOUND);
    		}
    		
    	}
    	employee.setReportingManager(reportingManager);

    	Employee savedEmployee = employeeRepository.save(employee);
    	
        return AppUtils.EmployeeToDto(savedEmployee);
    }
    
    
    public void updateEmployeeDepartmentById(Long id, Long deptId) {
   	 Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
       	 	Employee employee = employeeOptional.get();
       	 
	       	Department department = null;
	       	if(deptId != null) {
	       		department = departmentRepository.findById(deptId)
	       				.orElseThrow(() -> new DepartmentException("Department not found with ID: " + deptId,
	       				HttpStatus.NOT_FOUND));
	       	}
       	 
	       	employeeRepository.updateEmployeeDepartment(employee.getId(), department);
        }else {
       	 throw new EmployeeException("Employee not found with ID: " + id , HttpStatus.NOT_FOUND);
        }
   }
    
    
   public void updateEmployeeReportManagerById(Long id, Long reportManagerId) {
    	 Optional<Employee> employeeOptional = employeeRepository.findById(id);
         if (employeeOptional.isPresent()) {
        	 Employee employee = employeeOptional.get();
        	 
        	 Employee reportingManager = null;
        	 if(reportManagerId != null) {
        		 reportingManager = employeeRepository.findById(reportManagerId)
        				 .orElseThrow(() -> new EmployeeException("Report Manager not found with ID: " + reportManagerId,
        						 HttpStatus.NOT_FOUND));
        		 
        		 if(reportingManager.getId().equals(employee.getId())) {
        			 throw new EmployeeException(
        					 "Employee and Reporting Manager not be same person",
        					 HttpStatus.NOT_FOUND);
        		 }
        	 }
     		
        	 employeeRepository.updateEmployeeReportManager(employee.getId(), reportingManager);
         }else {
        	 throw new EmployeeException("Employee not found with ID: " + id , HttpStatus.NOT_FOUND);
         }
    }

   
    public void deleteEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + id , HttpStatus.NOT_FOUND));
        
        employeeRepository.delete(employee);
    }
    
}

