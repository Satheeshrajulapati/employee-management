package com.satheesh.employee.management.repository;

import com.satheesh.employee.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e 
            FROM Employee e
            WHERE LOWER(e.employeeName) LIKE LOWER (CONCAT('%', :value, '%'))
            OR LOWER(e.email) LIKE LOWER(CONCAT('%', :value, '%'))
            """)
    List <Employee> searchEmployees(@Param("value") String value);
}
