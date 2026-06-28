package com.example.ems.service.imp;

import com.example.ems.dto.DepartmentDto;
import com.example.ems.dto.requests.CreateDepartmentRequest;
import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import com.example.ems.helper.Pagination;
import com.example.ems.mapper.DepartmentMapper;
import com.example.ems.repo.DepartmentRepo;
import com.example.ems.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class DepartmentService {
    private final DepartmentMapper departmentMapper;
    private final DepartmentRepo departmentRepository;
    private final EmployeeRepo employeeRepo;

    public Page<Department> getAllDepartments(int page, int size) {
        return departmentRepository.findAll(Pagination.pageable(page, size, Sort.by("id").descending()));
    }
    public Department getDepartmentById(UUID id) {
        return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
    }
        public DepartmentDto createDepartment(CreateDepartmentRequest departmentRequest) {
            Department existingDepartment =departmentRepository.findByName(departmentRequest.name());
            if(existingDepartment != null) {
                throw new RuntimeException("Department already exists");
            }
            Department department = departmentMapper.toDepartment(departmentRequest);
           departmentRepository.save(department);
           return departmentMapper.toDepartmentDto(department);
        }
    @Transactional
        public DepartmentDto updateDepartment(UUID id, CreateDepartmentRequest departmentRequest) {
            Department existingDepartment = departmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            existingDepartment.setName(departmentRequest.name());
            existingDepartment.setBudget(departmentRequest.budget());
            existingDepartment.setLocation(departmentRequest.location());
            departmentRepository.save(existingDepartment);
            return departmentMapper.toDepartmentDto(existingDepartment);
        }
        @Transactional
        public DepartmentDto addEmployeeToDepartment(UUID departmentId, UUID employeeId) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            Employee employee= getEmployeeById(employeeId,departmentId);
            employee.setDepartment(department);
            employeeRepo.save(employee);
            department.getEmployees().add(employee);
            departmentRepository.save(department);
            return departmentMapper.toDepartmentDto(department);
        }
        @Transactional
        public DepartmentDto addEmployeesToDepartment(UUID departmentId, List<UUID> employeeIds) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            for (UUID employeeId : employeeIds) {
                Employee employee = getEmployeeById(employeeId,departmentId);
                employee.setDepartment(department);
                employeeRepo.save(employee);
                department.getEmployees().add(employee);
            }
            departmentRepository.save(department);
            return departmentMapper.toDepartmentDto(department);
        }

        @Transactional
        public void deleteDepartment(UUID id) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        if (!existingDepartment.getEmployees().isEmpty()) {
            throw new RuntimeException("Cannot delete department with employees");
        }
        departmentRepository.delete(existingDepartment);
    }

    private  Employee getEmployeeById(UUID id,UUID departmentId) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        if (employee.getDepartment() != null && !employee.getDepartment().getId().equals(departmentId)) {
            throw new RuntimeException("Employee already belongs to another department");
        }
        return employee;
    }
}
