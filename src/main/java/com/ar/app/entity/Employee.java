package com.ar.app.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate dateOfBirth;
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private String address;
    private String role;
    private LocalDate joiningDate;
    private Float yearlyBonusPercentage;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reporting_manager_id")
    private Employee reportingManager;
}
