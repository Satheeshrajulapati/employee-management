package com.satheesh.employee.management.dto.dashboard;

import com.satheesh.employee.management.dto.EmployeeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardResponseDto {

    private long totalEmployees;

    private int totalDepartments;

    private long activeUsers;

    private long totalAdmins;

    private List<DepartmentStatsDto> employeesByDepartment;

    private List<JoiningTrendDto> joiningTrend;

    private List<EmployeeResponseDto> recentEmployees;
}