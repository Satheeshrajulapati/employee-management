package com.satheesh.employee.management.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoiningTrendDto {

    private Integer year;

    private Integer month;

    private Long employeeCount;
}