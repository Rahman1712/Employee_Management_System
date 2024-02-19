package com.ar.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ar.app.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{

}
