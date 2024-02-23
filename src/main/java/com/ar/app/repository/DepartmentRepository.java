package com.ar.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ar.app.entity.Department;
import com.ar.app.entity.Employee;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>,
PagingAndSortingRepository<Department, Long>{

	boolean existsByName(String name);
	
    Optional<Department> findByName(String expand);
	
	@Transactional
    @Modifying
    @Query("UPDATE Department d SET d.departmentHead = :departmentHead WHERE d.id = :deptId")
    void updateDepartmentDepartmentHead(@Param("deptId") Long deptId, Employee departmentHead);
  
}

