package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.EmployeeRequestDto;
import com.satheesh.employee.management.dto.EmployeeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    List<EmployeeResponseDto> getAllEmployees();

    Page<EmployeeResponseDto> getEmployees(
            String search,
            String department,
            Pageable pageable
    );

    EmployeeResponseDto saveEmployee(EmployeeRequestDto dto);

    EmployeeResponseDto updateEmployee(
            Long id,
            EmployeeRequestDto dto
    );

    EmployeeResponseDto getEmployeeById(Long id);

    void deleteEmployee(Long id);
}