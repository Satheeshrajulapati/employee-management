package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.EmployeeRequestDto;
import com.satheesh.employee.management.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    void saveEmployee(EmployeeRequestDto dto);

    void updateEmployee(Long id, EmployeeRequestDto dto);

    Employee getEmployeeById(Long id);

    void deleteEmployee(Long id);
}
