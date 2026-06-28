package com.example.ems.service;

import com.example.ems.dto.requests.CreatEmployeeRequest;
import com.example.ems.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface EmployeeServiceF {
    List<Employee> getAllEmployees();

    List<Employee> createEmployees(List<CreatEmployeeRequest> employeesRequests);

    Employee getEmployeeById(UUID id);

    Employee createEmployee(CreatEmployeeRequest employeeRequest);

    Employee findEmployeeById(UUID id);

    Page<Employee> getAllEmployees(int page, int size);

    Page<Employee> getEmployeesByProject(int page, int size, UUID projectID);

    Page<Employee> getEmployeesByDepartment(UUID departmentId, int page, int size);

    Employee updateEmployeeInfo(UUID employeeId, CreatEmployeeRequest employeeRequest);

    Employee changeEmployeeDepartment(UUID employeeId, UUID departmentId);

    void deleteEmployee(UUID employeeId);
}
