package com.example.ems.repo;

import com.example.ems.entity.Employee;
import com.example.ems.entity.EmployeeProject;
import com.example.ems.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {

@EntityGraph(attributePaths = {"department", "employeeProjects"})
    Page<Employee> findByDepartmentId(UUID departmentId, Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Page<Employee> findDistinctByEmployeeProjectsProject(
            Project project,
            Pageable pageable
    );}
