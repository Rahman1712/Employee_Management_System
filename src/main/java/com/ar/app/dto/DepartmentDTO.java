package com.ar.app.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
	private Long id;
    private String name;
    private LocalDate creationDate;
    private Long departmentHeadId;
}