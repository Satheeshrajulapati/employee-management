package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.EmployeeRequestDto;
import com.satheesh.employee.management.entity.Employee;
import com.satheesh.employee.management.exception.EmployeeNotFoundException;
import com.satheesh.employee.management.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @Override
    public void saveEmployee(EmployeeRequestDto dto){
        Employee employee = new Employee();

        employee.setEmployeeName(dto.getEmployeeName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());

        employeeRepository.save(employee);
    }

    public void updateEmployee(Long id, EmployeeRequestDto dto){
        Employee employee = employeeRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setEmployeeName(dto.getEmployeeName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());

        employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id).orElseThrow(() ->
                new EmployeeNotFoundException("Employee not found with id: " + id ));
    }

    @Override
    public void deleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " +id
                        ));

        employeeRepository.delete(employee);
    }
}
