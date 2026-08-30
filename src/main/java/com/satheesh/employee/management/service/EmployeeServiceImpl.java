package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.EmployeeRequestDto;
import com.satheesh.employee.management.dto.EmployeeResponseDto;
import com.satheesh.employee.management.entity.Employee;
import com.satheesh.employee.management.exception.EmployeeNotFoundException;
import com.satheesh.employee.management.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    @Override
    public EmployeeResponseDto saveEmployee(EmployeeRequestDto dto) {

        Employee employee = new Employee();

        setEmployeeFields(employee, dto);

        Employee savedEmployee = employeeRepository.save(employee);

        return convertToResponseDto(savedEmployee);
    }

    @Override
    public EmployeeResponseDto updateEmployee(
            Long id,
            EmployeeRequestDto dto) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id
                        ));

        setEmployeeFields(employee, dto);

        Employee updatedEmployee = employeeRepository.save(employee);

        return convertToResponseDto(updatedEmployee);
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id
                        ));

        return convertToResponseDto(employee);
    }

    @Override
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id
                        ));

        employeeRepository.delete(employee);
    }

    // Request DTO → Entity
    private void setEmployeeFields(
            Employee employee,
            EmployeeRequestDto dto) {

        employee.setEmployeeName(dto.getEmployeeName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());
    }

    // Entity → Response DTO
    private EmployeeResponseDto convertToResponseDto(Employee employee) {

        EmployeeResponseDto responseDto = new EmployeeResponseDto();

        responseDto.setId(employee.getId());
        responseDto.setEmployeeName(employee.getEmployeeName());
        responseDto.setEmail(employee.getEmail());
        responseDto.setDepartment(employee.getDepartment());
        responseDto.setSalary(employee.getSalary());
        responseDto.setJoiningDate(employee.getJoiningDate());

        return responseDto;
    }

   public List<EmployeeResponseDto>searchEmployees(String value) {

        List<Employee> employees = employeeRepository.searchEmployees(value);

        return employees.stream().map(this::convertToResponseDto).toList();
   }
}