package com.example.Pagination.service;
import com.example.Pagination.entity.Employee;
import com.example.Pagination.repo.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    public Page<Employee> getAll(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return repo.findAll(paging);
    }
}
