package com.example.Pagination.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Pagination.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
