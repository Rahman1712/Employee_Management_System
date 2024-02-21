package com.ar.app.repository;

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
public interface EmployeeRepository extends JpaRepository<Employee, Long> ,
PagingAndSortingRepository<Employee, Long>
{

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.department = :department WHERE e.id = :employeeId")
    void updateEmployeeDepartment(@Param("employeeId") Long employeeId, Department department);
    
    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.reportingManager = :reportManager WHERE e.id = :employeeId")
    void updateEmployeeReportManager(@Param("employeeId") Long employeeId, @Param("reportManager") Employee reportManager);
    
}
