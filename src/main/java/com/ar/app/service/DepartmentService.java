package com.ar.app.service;

import java.time.LocalDate;
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

import com.ar.app.dto.DepartmentDTO;
import com.ar.app.dto.DepartmentRequest;
import com.ar.app.dto.EmployeeInfo;
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
    
//  Get Department By Id -----------------------------------------
    public DepartmentDTO getDepartmentById(Long id) {
    	Department department = departmentRepository.findById(id)
    			.orElseThrow(() -> new DepartmentException(
                		"Department not Found with ID: " + id,
                		HttpStatus.NOT_FOUND));
    	
    	return AppUtils.departmentToDto(department);
    }
    
//  Get All Department -----------------------------------------
    public List<DepartmentDTO> getAllDepartments() {
    	List<DepartmentDTO> departments = departmentRepository.findAll().stream()
    			.map(AppUtils::departmentToDto)
    			.collect(Collectors.toList());
    	
    	return departments;
    }
    
//  Get Department With Pagination -----------------------------------------
	public Map<String, Object> getDepartmentsByPage(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_LIMIT); 
		Page<Department> page = departmentRepository.findAll(pageable);
		
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		List<DepartmentDTO> departments = page.getContent().stream()
				.map(AppUtils::departmentToDto).collect(Collectors.toList());

		Map<String, Object> map = new HashMap<>();
		map.put("pageNum", pageNum);
		map.put("totalItems", totalItems);
		map.put("totalPages", totalPages);
		map.put("departments", departments);
		
		return map;
	}
	
//  Get Department With Employees -----------------------------------------
	public Map<String, Object> getDepartmentWithEmployees(String expand) {
		Optional<Department> departmentOptional = departmentRepository.findByName(expand);

		if(departmentOptional.isPresent()) {
			Department department = departmentOptional.get();
			
			List<EmployeeInfo> employees = department.getEmployees()
					.stream()
					.map(AppUtils::employeeToInfo)
					.collect(Collectors.toList());

			Map<String, Object> map = new HashMap<>();
			map.put("id", department.getId());
			map.put("name", department.getName());
			map.put("creationDate", department.getCreationDate());
			map.put("departmentHead",
					department.getDepartmentHead() != null ? 
						AppUtils.employeeToInfo(department.getDepartmentHead()) : 
							null);
			map.put("employees", employees);
			
			return map;
		}else {
			return null;
		}
	}
	
//  Get Department With Employees Expand -----------------------------------------
	public Map<String, Object> getDepartmentWithEmployees(String expand, Long id) {
    	Department department = departmentRepository.findById(id)
    			.orElseThrow(() -> new DepartmentException(
                		"Department not Found with ID: " + id,
                		HttpStatus.NOT_FOUND));

    	Map<String, Object> map = new HashMap<>();
    	
    	map.put("id", department.getId());
    	map.put("name", department.getName());
    	map.put("creationDate", department.getCreationDate());
    	map.put("departmentHead",
    			department.getDepartmentHead() != null ? 
    					AppUtils.employeeToInfo(department.getDepartmentHead()) : 
    						null);
    	
		if(expand != null && expand.equals("employee")) {
			List<EmployeeInfo> employees = department.getEmployees()
					.stream()
					.map(AppUtils::employeeToInfo)
					.collect(Collectors.toList());

			map.put("employees", employees);
		}
		
		return map;
	}

//  Get Departments With Expanded  -----------------------------------------
	public Map<String, Object> getDepartmentsWithExpand(String expand, boolean paginate, int pageNum) {

		Map<String, Object> responseMap = new HashMap<>();
		List<Department> departments = null;
		
		if(paginate) {
			Pageable pageable = PageRequest.of(pageNum - 1, PAGE_LIMIT); 
			Page<Department> page = departmentRepository.findAll(pageable);
			long totalItems = page.getTotalElements();
			int totalPages = page.getTotalPages();
			departments = page.getContent();
			
			responseMap.put("pageNum", pageNum);
			responseMap.put("totalItems", totalItems);
			responseMap.put("totalPages", totalPages);
			
		}else {
			departments = departmentRepository.findAll();
		}
		
		if(expand != null && expand.equals("employee")) {
			var departmentsExpanded = departments.stream().map(dept -> getDepartmentData(dept)).collect(Collectors.toList());
			
			responseMap.put("departments", departmentsExpanded);
		}else {
			List<DepartmentDTO> departmentsDto = departments.stream()
					.map(AppUtils::departmentToDto).collect(Collectors.toList());
			
			responseMap.put("departments", departmentsDto);
		}
		
		return responseMap;
	}
	
//	Get Department Data  ---------   
	private Map<String, Object> getDepartmentData(Department department){
		
		List<EmployeeInfo> employees = department.getEmployees()
				.stream()
				.map(AppUtils::employeeToInfo)
				.collect(Collectors.toList());
		
		Map<String, Object> map = new HashMap<>();
		map.put("id", department.getId());
		map.put("name", department.getName());
		map.put("creationDate", department.getCreationDate());
		map.put("departmentHead",
				department.getDepartmentHead() != null ? 
					AppUtils.employeeToInfo(department.getDepartmentHead()) : 
						null);
		map.put("employees", employees);
		
		return map;
	}
	
//  Create Department -----------------------------------------
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
        
        return AppUtils.departmentToDto(saved);
    }
    
//  Update Department By Id -----------------------------------------
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
        
        if(departmentRequest.getDepartmentHeadId() != null) {
    		Employee deptHead = employeeRepository.findById(departmentRequest.getDepartmentHeadId())
    			.orElseThrow(() -> new EmployeeException("Employee not Found with ID: "+departmentRequest.getDepartmentHeadId(),HttpStatus.NOT_FOUND));
    		
    		department.setDepartmentHead(deptHead);
    	}

        department = departmentRepository.save(department);

        return AppUtils.departmentToDto(department);
    }
    
//  Update Department Head By Id -----------------------------------------
    public void updateDepartmentHeadById(Long id, Long deptHeadId) {
    	Department department = departmentRepository.findById(id)
    			.orElseThrow(() -> new DepartmentException("Department not found with ID: " + id , HttpStatus.NOT_FOUND));
        
    	Employee deptHead = employeeRepository.findById(deptHeadId)
    			.orElseThrow(() -> new EmployeeException("Department Head :  Employee not Found with ID: "+deptHeadId,HttpStatus.NOT_FOUND));

    	departmentRepository.updateDepartmentDepartmentHead(department.getId(), deptHead);
    }
    
//  Delete Department By Id -----------------------------------------
    public void deleteDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentException("Department not found with ID: " + id , HttpStatus.NOT_FOUND));
        
        if (!department.getEmployees().isEmpty()) {
            throw new DepartmentException(
            		"Department cannot be deleted because it has associated employees.",
            		HttpStatus.BAD_REQUEST);
        }
        
        departmentRepository.delete(department);
    }

}
