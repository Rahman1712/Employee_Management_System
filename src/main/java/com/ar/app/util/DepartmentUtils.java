package com.ar.app.util;

import com.ar.app.dto.DepartmentDTO;
import com.ar.app.entity.Department;

public class DepartmentUtils {
	
	public static DepartmentDTO toDto(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setName(department.getName());
        departmentDTO.setCreationDate(department.getCreationDate());
        if (department.getDepartmentHead() != null) {
            departmentDTO.setDepartmentHeadId(department.getDepartmentHead().getId());
        }
        return departmentDTO;
    }
}
