package com.example.XLSXDataLoader.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.XLSXDataLoader.entity.Employee;
import com.example.XLSXDataLoader.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelService {

    private final EmployeeRepository repository;

    @Transactional
    public String processExcel(MultipartFile file) throws IOException {
        List<Employee> employees = new ArrayList<>();
        int updated = 0;
        int inserted = 0;

        // 🔥 Read Excel (YOUR ORIGINAL CODE)
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                Employee newEmp = new Employee();
                newEmp.setName(getCellValue(row.getCell(0)));
                newEmp.setEmail(getCellValue(row.getCell(1)));
                newEmp.setDepartment(getCellValue(row.getCell(2)));
                newEmp.setSalary(getNumericCellValue(row.getCell(3)));

                employees.add(newEmp);
            }
        }

        // 🔥 UPSERT LOGIC: Replace OR Insert
        for (Employee newEmp : employees) {
            Optional<Employee> existing = repository.findByEmail(newEmp.getEmail());
            
            if (existing.isPresent()) {
                // UPDATE existing record
                Employee emp = existing.get();
                emp.setName(newEmp.getName());
                emp.setDepartment(newEmp.getDepartment());
                emp.setSalary(newEmp.getSalary());
                repository.save(emp);
                updated++;
            } else {
                // INSERT new record
                repository.save(newEmp);
                inserted++;
            }
        }

        return String.format("✅ Success! Updated %d, Inserted %d (%d total)", 
                            updated, inserted, updated + inserted);
    }

    // 🔥 YOUR ORIGINAL HELPER METHODS (UNCHANGED)
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }

    private Double getNumericCellValue(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
        return cell.getNumericCellValue();
    }
}

