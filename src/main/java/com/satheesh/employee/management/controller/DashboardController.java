package com.satheesh.employee.management.controller;

import com.satheesh.employee.management.dto.dashboard.DashboardResponseDto;
import com.satheesh.employee.management.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardResponseDto> getDashboardStats() {

        DashboardResponseDto response =
                dashboardService.getDashboardStats();

        return ResponseEntity.ok(response);
    }
}