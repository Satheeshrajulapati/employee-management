package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.EmployeeRequestDto;
import com.satheesh.employee.management.dto.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {

    List<EmployeeResponseDto> getAllEmployees();

    EmployeeResponseDto saveEmployee(EmployeeRequestDto dto);

    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto);

    EmployeeResponseDto getEmployeeById(Long id);

    void deleteEmployee(Long id);

    List<EmployeeResponseDto> searchEmployees(String value);
}