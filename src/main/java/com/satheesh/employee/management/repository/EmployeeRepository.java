package com.satheesh.employee.management.repository;

import com.satheesh.employee.management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e
            FROM Employee e
            WHERE (
                :search = ''
                OR LOWER(e.employeeName)
                    LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(e.email)
                    LIKE LOWER(CONCAT('%', :search, '%'))
            )
            AND (
                :department = ''
                OR e.department = :department
            )
            """)
    Page<Employee> findEmployees(
            @Param("search") String search,
            @Param("department") String department,
            Pageable pageable
    );

    @Query("""
            SELECT e
            FROM Employee e
            WHERE (
                :search = ''
                OR LOWER(e.employeeName)
                    LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(e.email)
                    LIKE LOWER(CONCAT('%', :search, '%'))
            )
            AND (
                :department = ''
                OR e.department = :department
            )
            ORDER BY e.id ASC
            """)
    List<Employee> findEmployeesForExport(
            @Param("search") String search,
            @Param("department") String department
    );
}