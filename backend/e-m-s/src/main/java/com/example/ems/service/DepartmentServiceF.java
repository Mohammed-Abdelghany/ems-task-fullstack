package com.example.ems.service;

import com.example.ems.dto.DepartmentDto;
import com.example.ems.dto.requests.CreateDepartmentRequest;
import com.example.ems.entity.Department;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface DepartmentServiceF {
    Page<Department> getAllDepartments(int page, int size);

    Department getDepartmentById(UUID id);

    DepartmentDto createDepartment(CreateDepartmentRequest departmentRequest);

    DepartmentDto updateDepartment(UUID id, CreateDepartmentRequest departmentRequest);

    DepartmentDto addEmployeeToDepartment(UUID departmentId, UUID employeeId);

    DepartmentDto addEmployeesToDepartment(UUID departmentId, List<UUID> employeeIds);

    void deleteDepartment(UUID id);}
