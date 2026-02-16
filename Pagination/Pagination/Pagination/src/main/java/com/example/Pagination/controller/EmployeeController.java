package com.example.Pagination.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Pagination.entity.Employee;
import com.example.Pagination.service.EmployeeService;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping("/employee")
    public Page<Employee> getPaginated(
            @RequestParam(defaultValue = "0") int pageNo) {

        int pageSize = 10; 

        return service.getAll(pageNo-1, pageSize);
    }
}