package com.satheesh.employee.management.service;

import com.satheesh.employee.management.dto.EmployeeResponseDto;
import com.satheesh.employee.management.dto.dashboard.DashboardResponseDto;
import com.satheesh.employee.management.dto.dashboard.DepartmentStatsDto;
import com.satheesh.employee.management.dto.dashboard.JoiningTrendDto;
import com.satheesh.employee.management.entity.Employee;
import com.satheesh.employee.management.entity.Role;
import com.satheesh.employee.management.projection.DepartmentEmployeeCount;
import com.satheesh.employee.management.projection.EmployeeJoiningTrend;
import com.satheesh.employee.management.repository.EmployeeRepository;
import com.satheesh.employee.management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl
        implements DashboardService {

    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;

    public DashboardServiceImpl(
            EmployeeRepository employeeRepository,
            UserRepository userRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DashboardResponseDto getDashboardStats() {

        long totalEmployees =
                employeeRepository.count();

        long activeUsers =
                userRepository.countByEnabledTrue();

        long totalAdmins =
                userRepository.countByRoleIn(
                        List.of(
                                Role.ADMIN,
                                Role.SUPER_ADMIN
                        )
                );

        List<DepartmentStatsDto> departmentStats =
                employeeRepository
                        .countEmployeesByDepartment()
                        .stream()
                        .map(this::mapDepartmentStats)
                        .toList();

        List<JoiningTrendDto> joiningTrend =
                buildJoiningTrend();

        List<EmployeeResponseDto> recentEmployees =
                employeeRepository
                        .findTop5ByOrderByJoiningDateDescIdDesc()
                        .stream()
                        .map(this::mapEmployee)
                        .toList();

        return new DashboardResponseDto(
                totalEmployees,
                departmentStats.size(),
                activeUsers,
                totalAdmins,
                departmentStats,
                joiningTrend,
                recentEmployees
        );
    }

    private DepartmentStatsDto mapDepartmentStats(
            DepartmentEmployeeCount projection
    ) {
        return new DepartmentStatsDto(
                projection.getDepartment(),
                projection.getEmployeeCount()
        );
    }

    private JoiningTrendDto mapJoiningTrend(
            EmployeeJoiningTrend projection
    ) {
        return new JoiningTrendDto(
                projection.getYear(),
                projection.getMonth(),
                projection.getEmployeeCount()
        );
    }

    private EmployeeResponseDto mapEmployee(
            Employee employee
    ) {
        EmployeeResponseDto dto =
                new EmployeeResponseDto();

        dto.setId(employee.getId());

        dto.setEmployeeName(
                employee.getEmployeeName()
        );

        dto.setEmail(
                employee.getEmail()
        );

        dto.setDepartment(
                employee.getDepartment()
        );

        dto.setSalary(
                employee.getSalary()
        );

        dto.setJoiningDate(
                employee.getJoiningDate()
        );

        return dto;
    }

    private List<JoiningTrendDto> buildJoiningTrend() {

        List<JoiningTrendDto> existingTrend =
                employeeRepository
                        .getEmployeeJoiningTrend()
                        .stream()
                        .map(this::mapJoiningTrend)
                        .toList();

        if (existingTrend.isEmpty()) {
            return List.of();
        }

        JoiningTrendDto first =
                existingTrend.get(0);

        JoiningTrendDto last =
                existingTrend.get(
                        existingTrend.size() - 1
                );

        YearMonth start =
                YearMonth.of(
                        first.getYear(),
                        first.getMonth()
                );

        YearMonth end =
                YearMonth.of(
                        last.getYear(),
                        last.getMonth()
                );

        List<JoiningTrendDto> completeTrend =
                new ArrayList<>();

        YearMonth current = start;

        while (!current.isAfter(end)) {

            YearMonth currentMonth = current;

            long employeeCount =
                    existingTrend
                            .stream()
                            .filter(item ->
                                    item.getYear()
                                            .equals(
                                                    currentMonth.getYear()
                                            )
                                            &&
                                            item.getMonth()
                                                    .equals(
                                                            currentMonth.getMonthValue()
                                                    )
                            )
                            .mapToLong(
                                    JoiningTrendDto::getEmployeeCount
                            )
                            .findFirst()
                            .orElse(0L);

            completeTrend.add(
                    new JoiningTrendDto(
                            current.getYear(),
                            current.getMonthValue(),
                            employeeCount
                    )
            );

            current =
                    current.plusMonths(1);
        }

        return completeTrend;
    }
}