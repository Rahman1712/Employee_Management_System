package com.ar.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.app.dto.EmployeeDTO;
import com.ar.app.dto.EmployeeRequest;
import com.ar.app.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	@Autowired
    private EmployeeService employeeService;
	
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
    	EmployeeDTO createdEmployee = employeeService.createEmployee(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO>  getEmployeeById(@PathVariable Long id) {
    	EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeDTO);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDTO>>  getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    
    @GetMapping("/employees-bypage")
    public ResponseEntity<Map<String, Object>>  getEmployeesByPage(@RequestParam(defaultValue = "1") int pageNum) {
        return ResponseEntity.ok(employeeService.getEmployeesByPage(pageNum));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@PathVariable Long id, @RequestBody EmployeeRequest request) {
    	EmployeeDTO updatedEmployee = employeeService.updateEmployeeById(id, request);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    @PatchMapping("/update-dept-byid/{id}")
    public ResponseEntity<String> updateEmployeeDepartmentById(@PathVariable Long id, @RequestParam("deptId") Long deptId) {
    	employeeService.updateEmployeeDepartmentById(id, deptId);
        return ResponseEntity.ok("Employee department updated successfully");
    }
    
    @PatchMapping("/update-reportmanager-byid/{id}")
    public ResponseEntity<String> updateEmployeeReportManagerById(@PathVariable Long id, @RequestParam("reportManagerId") Long reportManagerId) {
    	employeeService.updateEmployeeReportManagerById(id, reportManagerId);
    	return ResponseEntity.ok("Employee Reporting Manager updated successfully");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(Long id) {
    	employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
    
	
}
