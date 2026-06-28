package com.example.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<EmployeeProject> employees;
    @ManyToMany
    @JoinTable(
            name = "project_department",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )

    private List<Department> departments;




}
