package com.satheesh.employee.management.controller;

import com.satheesh.employee.management.dto.EmployeeRequestDto;
import com.satheesh.employee.management.entity.Employee;
import com.satheesh.employee.management.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @PostMapping()
    public ResponseEntity<String>saveEmployee(@RequestBody EmployeeRequestDto dto){
        employeeService.saveEmployee(dto);
        return ResponseEntity.ok("Employee saved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String>updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequestDto dto){
        employeeService.updateEmployee(id, dto);
        return ResponseEntity.ok("Employee updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee>getEmployeeById(@PathVariable Long id){
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

}
