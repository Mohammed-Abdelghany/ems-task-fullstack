package com.example.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "departments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal budget;
    @Column(nullable = false)
    private String location;
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
    @ManyToMany(mappedBy = "departments")
    private List<Project> projects;


}
