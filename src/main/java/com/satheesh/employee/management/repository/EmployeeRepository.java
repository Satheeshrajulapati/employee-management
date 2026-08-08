package com.satheesh.employee.management.repository;

import com.satheesh.employee.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
