package com.example.ems.controller;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.dto.requests.CreatEmployeeRequest;
import com.example.ems.entity.Employee;
import com.example.ems.entity.Project;
import com.example.ems.mapper.EmployeeMapper;
import com.example.ems.repo.ProjectRepo;
import com.example.ems.service.imp.EmployeeService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final ProjectRepo projectRepo;

    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper, ProjectRepo projectRepo) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.projectRepo = projectRepo;
    }

    @PostMapping("/create-list")

    @ResponseStatus(
            HttpStatus.CREATED
    )
    public ResponseEntity<List<EmployeeDto>> createEmployees(@Valid  @RequestBody  List<CreatEmployeeRequest> employeesRequests) {
        List<Employee> Employees = employeeService.createEmployees(employeesRequests);
        List<EmployeeDto> employeeDtos = employeeMapper.toEmployeeDtoList(Employees);
        return ResponseEntity.ok(employeeDtos);
    }
    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody CreatEmployeeRequest employeeRequest) {
        Employee employee = employeeService.createEmployee(employeeRequest);
        EmployeeDto employeeDto = employeeMapper.toEmployeeDto(employee);
        return ResponseEntity.ok(employeeDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable UUID id) {
        Employee employee = employeeService.findEmployeeById(id);
        EmployeeDto employeeDto = employeeMapper.toEmployeeDto(employee);
        return ResponseEntity.ok(employeeDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<EmployeeDto>> getEmployees(int page, int size) {
        Page<Employee> employees = employeeService.getAllEmployees(page , size);
        return ResponseEntity.ok(employees.map(
                employee -> employeeMapper.toEmployeeDto(employee)
        ));
    }

    @GetMapping("/department/{departmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<EmployeeDto>> getEmployeesByDepartment(@PathVariable UUID departmentId, int page, int size) {
        Page<Employee> employees = employeeService.getEmployeesByDepartment(departmentId, page, size);
        return ResponseEntity.ok(employees.map(
                employee -> employeeMapper.toEmployeeDto(employee)
        ));
    }

    @GetMapping("/project/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<EmployeeDto>> getEmployeesByProject(@PathVariable UUID projectId, int page, int size) {
        Page<Employee> employees = employeeService.getEmployeesByProject(page, size, projectId);
        return ResponseEntity.ok(employees.map(
                employee -> employeeMapper.toEmployeeDto(employee)
        ));
    }

    @PutMapping("update-info/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeDto> updateEmployeeInfo(@PathVariable UUID employeeId, @RequestBody CreatEmployeeRequest employeeRequest) {
        Employee updatedEmployee = employeeService.updateEmployeeInfo(employeeId, employeeRequest);
        EmployeeDto employeeDto = employeeMapper.toEmployeeDto(updatedEmployee);
        return ResponseEntity.ok(employeeDto);
    }

    @PutMapping("change-department/{employeeId}/{departmentId}")
    public ResponseEntity<EmployeeDto> changeDepartment(@PathVariable UUID employeeId, @PathVariable UUID departmentId) {
        Employee employee = employeeService.changeEmployeeDepartment(employeeId, departmentId);
        EmployeeDto employeeDto = employeeMapper.toEmployeeDto(employee);
        return ResponseEntity.ok(employeeDto);
    }

    @DeleteMapping("delete/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }







}



