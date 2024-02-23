package com.ar.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ar.app.dto.EmployeeDTO;
import com.ar.app.dto.EmployeeInfo;
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

//  Create Employee -----------------------------------------
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
    		Employee reportingManager = employeeRepository.findById(request.getReportingManagerId())
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
        
        return AppUtils.employeeToDto(savedEmployee);
    }
    
//  Get Employee by ID -----------------------------------------
    public EmployeeDTO getEmployeeById(Long id) {
    	Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeException(
                		"Employee not Found with ID: " + id,
                		HttpStatus.NOT_FOUND));
    	
    	return AppUtils.employeeToDto(employee);
    }
    
//  Get All Employees -----------------------------------------
    public List<EmployeeDTO> getAllEmployees() {
    	List<EmployeeDTO> employeesList = employeeRepository.findAll().stream()
    			.map(AppUtils::employeeToDto)
    			.collect(Collectors.toList());
    	return employeesList;
    }

//  Get Employees with Pagination -----------------------------------------
    public Map<String, Object> getEmployeesByPage(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_LIMIT); 
		Page<Employee> page = employeeRepository.findAll(pageable);
		
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		
		List<EmployeeDTO> employees = page.getContent().stream()
				.map(AppUtils::employeeToDto).collect(Collectors.toList());
		
		return getEmployeesMapData(pageNum, totalItems, totalPages, employees);
	}
    
//  Get Employee's Info (Id and Name) with Pagination -------------------------------
	public Map<String, Object> getAllEmployeeInfos(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_LIMIT); 
		Page<Employee> page = employeeRepository.findAll(pageable);
		
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		List<EmployeeInfo> employeeInfos = page.getContent().stream()
				.map(AppUtils::employeeToInfo).collect(Collectors.toList());

		return getEmployeesMapData(pageNum, totalItems, totalPages, employeeInfos);
	}
	
//  Map Data--------------------------------------------------
    private Map<String, Object> getEmployeesMapData(
    		int pageNum, long totalItems, int totalPages, Object employees) {
    	
		Map<String, Object> map = new HashMap<>();
		map.put("pageNum", pageNum);
		map.put("totalItems", totalItems);
		map.put("totalPages", totalPages);
		map.put("employees", employees);
		
		return map;
	}
    
//  Update Employee by ID -----------------------------------------
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
    	
        return AppUtils.employeeToDto(savedEmployee);
    }
    
//  Update Employee's Department by ID -----------------------------------------
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
    
// Update Employee's Reporting Manager by ID ---------------------------------------
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
   
//  Check is ReportingManager -----------------------------------------
    private boolean isReportingManager(Employee employee) {
       return employeeRepository.existsByReportingManager(employee);
    }
    
//  Delete Employee by ID -----------------------------------------
    public void deleteEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeException("Employee not found with ID: " + id , HttpStatus.NOT_FOUND));
        
        if (isReportingManager(employee)) {
            throw new EmployeeException(
            		"Cannot delete employee because they are set as a reporting manager for other employees.",
            		HttpStatus.BAD_REQUEST);
        }
        
        employeeRepository.delete(employee);
    }
    
    public Map<Long, List<Long>> getReportings(Long userId) {
        // Retrieve the employee by ID
        Employee employee = employeeRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + userId));

        // Initialize a map to store reporting managers and their employees
        Map<Long, List<Long>> managerEmployeesMap = new HashMap<>();

        // Retrieve all employees
        List<Employee> allEmployees = employeeRepository.findAll();

        // Populate the map with reporting managers and their employees
        for (Employee emp : allEmployees) {
            Long managerId = emp.getReportingManager().getId();
            managerEmployeesMap.computeIfAbsent(managerId, k -> new ArrayList<>()).add(emp.getId());
        }

        
        Queue<Long> queue = new LinkedList<>();
        
        queue.forEach(x -> System.out.println(x+" "));
        
        return managerEmployeesMap;
    }
    
    public void rec(Map<Long, List<Long>> managerEmployeesMap, Queue<Long> queue, Long userId) {
    	List<Long> list = managerEmployeesMap.get(userId);
    	for(Long x: list) {
        	queue.add(x);
        	rec(managerEmployeesMap, queue, x);
        }
    }
}

