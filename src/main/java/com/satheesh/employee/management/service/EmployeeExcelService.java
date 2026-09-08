package com.satheesh.employee.management.service;

import com.satheesh.employee.management.entity.Employee;
import com.satheesh.employee.management.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.util.CellRangeAddress;

@Service
public class EmployeeExcelService {

    private final EmployeeRepository employeeRepository;

    public EmployeeExcelService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public byte[] exportEmployees(
            String search,
            String department
    ) throws IOException {

        List<Employee> employees =
                employeeRepository.findEmployeesForExport(
                        search,
                        department
                );

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            Sheet sheet =
                    workbook.createSheet("Employees");

            // Freeze header row
            sheet.createFreezePane(0, 1);

            // =========================
            // Header Style
            // =========================

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle =
                    workbook.createCellStyle();

            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(
                    HorizontalAlignment.CENTER
            );

            // =========================
            // Salary Style
            // =========================

            CellStyle salaryStyle =
                    workbook.createCellStyle();

            DataFormat dataFormat =
                    workbook.createDataFormat();

            salaryStyle.setDataFormat(
                    dataFormat.getFormat("₹#,##0.00")
            );

            // =========================
            // Date Style
            // =========================

            CellStyle dateStyle =
                    workbook.createCellStyle();

            dateStyle.setDataFormat(
                    dataFormat.getFormat("dd-mmm-yyyy")
            );

            // =========================
            // Header Row
            // =========================

            String[] headers = {
                    "ID",
                    "Employee",
                    "Email",
                    "Department",
                    "Salary",
                    "Joining Date"
            };

            Row headerRow =
                    sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {

                Cell cell =
                        headerRow.createCell(i);

                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // =========================
            // Employee Data
            // =========================

            int rowIndex = 1;

            for (Employee employee : employees) {

                Row row =
                        sheet.createRow(rowIndex++);

                row.createCell(0)
                        .setCellValue(employee.getId());

                row.createCell(1)
                        .setCellValue(employee.getEmployeeName());

                row.createCell(2)
                        .setCellValue(employee.getEmail());

                row.createCell(3)
                        .setCellValue(employee.getDepartment());

                // Salary
                Cell salaryCell =
                        row.createCell(4);

                salaryCell.setCellValue(
                        employee.getSalary().doubleValue()
                );

                salaryCell.setCellStyle(
                        salaryStyle
                );

                // Joining Date
                Cell dateCell =
                        row.createCell(5);

                if (employee.getJoiningDate() != null) {

                    dateCell.setCellValue(
                            employee
                                    .getJoiningDate()
                                    .atStartOfDay()
                    );

                    dateCell.setCellStyle(
                            dateStyle
                    );
                }
            }

            // =========================
            // Auto Filter
            // =========================

            if (!employees.isEmpty()) {

                sheet.setAutoFilter(
                        new CellRangeAddress(
                                0,
                                employees.size(),
                                0,
                                headers.length - 1
                        )
                );
            }

            // =========================
            // Column Width
            // =========================

            for (int i = 0; i < headers.length; i++) {

                sheet.autoSizeColumn(i);

                // Give a little extra space
                sheet.setColumnWidth(
                        i,
                        Math.min(
                                sheet.getColumnWidth(i) + 1000,
                                255 * 256
                        )
                );
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();
        }
    }

}