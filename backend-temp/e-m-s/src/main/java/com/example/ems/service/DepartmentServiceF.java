package com.example.ems.service;

import com.example.ems.dto.requests.CreateDepartmentRequest;
import com.example.ems.entity.Department;

public interface DepartmentServiceF {
    Department createDepartment(CreateDepartmentRequest departmentRequest);
}
