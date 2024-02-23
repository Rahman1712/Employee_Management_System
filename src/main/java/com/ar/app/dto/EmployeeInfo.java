package com.ar.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeInfo {
	
	private Long id;
    private String name;
}

