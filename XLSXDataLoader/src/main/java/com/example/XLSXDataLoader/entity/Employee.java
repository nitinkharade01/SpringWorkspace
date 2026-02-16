package com.example.XLSXDataLoader.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employees")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String department;
    
    // ✅ FIXED: Use BigDecimal for currency or simple Double
    @Column(name = "salary")
    private Double salary;
}
