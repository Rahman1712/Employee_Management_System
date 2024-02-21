package com.ar.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.app.dto.DepartmentDTO;
import com.ar.app.dto.DepartmentRequest;
import com.ar.app.service.DepartmentService;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentRequest departmentRequest) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO>  getDepartmentById(@PathVariable Long id) {
    	DepartmentDTO departmentDTO = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(departmentDTO);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>>  getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
    
    @GetMapping("/departments-bypage")
    public ResponseEntity<Map<String, Object>>  getDepartmentsByPage(@RequestParam(defaultValue = "1") int pageNum) {
        return ResponseEntity.ok(departmentService.getDepartmentsByPage(pageNum));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartmentById(@PathVariable Long id, @RequestBody DepartmentRequest departmentRequest) {
        DepartmentDTO updatedDepartment = departmentService.updateDepartmentById(id, departmentRequest);
        return ResponseEntity.ok(updatedDepartment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartmentById(@PathVariable Long id) {
        departmentService.deleteDepartmentById(id);
        return ResponseEntity.noContent().build();
    }
    
}
