package com.satheesh.employee.management.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartmentStatsDto {

    private String department;
    private Long employeeCount;
}