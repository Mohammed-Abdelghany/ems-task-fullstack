package com.example.ems.service.imp;

import com.example.ems.dto.requests.CreatEmployeeRequest;
import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import com.example.ems.entity.Project;
import com.example.ems.helper.Pagination;
import com.example.ems.mapper.EmployeeMapper;
import com.example.ems.repo.DepartmentRepo;
import com.example.ems.repo.EmployeeProjectRepo;
import com.example.ems.repo.EmployeeRepo;
import com.example.ems.repo.ProjectRepo;
import com.example.ems.service.EmployeeServiceF;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements EmployeeServiceF {
    private final EmployeeRepo employeeRepository;
    private final DepartmentRepo departmentRepository;
    private final EmployeeMapper employeeMapper;
    private final ProjectRepo projectRepo;
    private final EmployeeProjectRepo employeeProjectRepo;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public List<Employee> createEmployees(List<CreatEmployeeRequest> employeesRequests) {
        employeesRequests.forEach(employeeRequest -> {
            validateEmployeeRequest(employeeRequest);
            Department department = departmentRepository.findById(employeeRequest.departmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found"));
            Employee employee = employeeMapper.toEmployee(employeeRequest);
            employee.setDepartment(department);
            log.info("Creating employee " + employee);
            employeeRepository.save(employee);
        });
        log.info("All employees created");
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }
    @Transactional
    public Employee createEmployee(CreatEmployeeRequest employeeRequest) {
        validateEmployeeRequest(employeeRequest);
        Employee employee = employeeMapper.toEmployee(employeeRequest);
        if (employeeRequest.departmentId() != null){
            Department department = departmentRepository.findById(employeeRequest.departmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        employee.setDepartment(department);
    }else {
            employee.setDepartment(null);
        }

        log.info("Creating employee " + employee);
        return employeeRepository.save(employee);
    }

    public Employee findEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }
    public Page<Employee> getAllEmployees(int page, int size) {
        return employeeRepository.findAll(Pagination.pageable(page, size, Sort.by("id").descending()));
    }
    public Page<Employee> getEmployeesByProject(int page, int size,UUID projectID) {
        Project project = projectRepo.findById(projectID)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        return employeeRepository.findDistinctByEmployeeProjectsProject(project, PageRequest.of(page, size));
    }

    public Page<Employee> getEmployeesByDepartment(UUID departmentId, int page, int size) {
        return employeeRepository.findByDepartmentId(departmentId,Pagination.pageable(page, size, Sort.by("id").descending()));
    }
    @Transactional
    public Employee updateEmployeeInfo(UUID employeeId, CreatEmployeeRequest employeeRequest) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        if (employeeRequest.name() != null) {
            employee.setName(employeeRequest.name());
        }
        if (employeeRequest.email() != null && !employeeRequest.email().equals(employee.getEmail())) {
            if (employeeRepository.existsByEmail(employeeRequest.email())) {
                throw new IllegalArgumentException("Employee with email " + employeeRequest.email() + " already exists.");
            }
            employee.setEmail(employeeRequest.email());
        }
        if (employeeRequest.phone() != null && !employeeRequest.phone().equals(employee.getPhone())) {
            if (employeeRepository.existsByPhone(employeeRequest.phone())) {
                throw new IllegalArgumentException("Employee with phone " + employeeRequest.phone() + " already exists.");
            }
            employee.setPhone(employeeRequest.phone());
        }
        if (employeeRequest.salary() != null) {
            if (employeeRequest.salary().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Salary must be greater than 0");
            }
            employee.setSalary(employeeRequest.salary());
        }
        if (employeeRequest.departmentId() != null) {
            Department  department = departmentRepository.findById(employeeRequest.departmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found"));
            employee.setDepartment(department);
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee changeEmployeeDepartment(UUID employeeId, UUID departmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }
    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        if (employee.getEmployeeProjects() != null && !employee.getEmployeeProjects().isEmpty()) {
            employeeProjectRepo.deleteAll(employee.getEmployeeProjects());
        }
        employeeRepository.delete(employee);
    }
    private void validateEmployeeRequest(CreatEmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.email())) {
            throw new IllegalArgumentException("Employee with email " + employeeRequest.email() + " already exists.");
        }
        if (employeeRepository.existsByPhone(employeeRequest.phone())) {
            throw new IllegalArgumentException("Employee with phone " + employeeRequest.phone() + " already exists.");
        }
        if (employeeRequest.salary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salary must be greater than 0");
        }
    }

}





