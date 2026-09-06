package com.satheesh.employee.management.controller;

import com.satheesh.employee.management.dto.EmployeeRequestDto;
import com.satheesh.employee.management.dto.EmployeeResponseDto;
import com.satheesh.employee.management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(
            EmployeeService employeeService
    ) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> getEmployees(

            @RequestParam(defaultValue = "")
            String search,

            @RequestParam(defaultValue = "")
            String department,

            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "id",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {

        Page<EmployeeResponseDto> employees =
                employeeService.getEmployees(
                        search,
                        department,
                        pageable
                );

        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> saveEmployee(
            @Valid @RequestBody EmployeeRequestDto dto
    ) {

        EmployeeResponseDto response =
                employeeService.saveEmployee(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto dto
    ) {

        EmployeeResponseDto response =
                employeeService.updateEmployee(
                        id,
                        dto
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(
            @PathVariable Long id
    ) {

        EmployeeResponseDto response =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id
    ) {

        employeeService.deleteEmployee(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/departments")
    public ResponseEntity<List<String>> getDepartments() {

        List<String> departments = Arrays.asList(
                "IT",
                "HR",
                "Finance",
                "Manager",
                "Engineering",
                "Marketing",
                "Executive"
        );

        return ResponseEntity.ok(departments);
    }
}