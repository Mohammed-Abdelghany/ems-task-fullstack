package com.example.ems.controller;

import com.example.ems.dto.DepartmentDto;
import com.example.ems.dto.requests.CreateDepartmentRequest;
import com.example.ems.service.imp.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;


    @GetMapping
    public ResponseEntity<Page<DepartmentDto>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<DepartmentDto> departments = departmentService.getAllDepartments(page, size).map(department -> new DepartmentDto(department.getId(), department.getName(), department.getBudget(), department.getLocation()));
        return ResponseEntity.ok(departments);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DepartmentDto> createDepartment( @Valid @RequestBody CreateDepartmentRequest request) {
        DepartmentDto departmentDto = departmentService.createDepartment(request);
        return ResponseEntity.ok(departmentDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable("id") UUID id, @Valid @RequestBody CreateDepartmentRequest request) {
        DepartmentDto departmentDto = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(departmentDto);
    }

    @PostMapping("add-employee/{departmentId}/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DepartmentDto> addEmployeeToDepartment(@PathVariable("departmentId") UUID departmentId, @PathVariable("employeeId") UUID employeeId) {
        DepartmentDto departmentDto = departmentService.addEmployeeToDepartment(departmentId, employeeId);
        return ResponseEntity.ok(departmentDto);
    }
    @PostMapping("add-employees/{departmentId}")
    public ResponseEntity<DepartmentDto> addEmployeesToDepartment(@PathVariable("departmentId") UUID departmentId, @RequestBody List<UUID> employeeIds) {
        DepartmentDto departmentDto = departmentService.addEmployeesToDepartment(departmentId, employeeIds);
        return ResponseEntity.ok(departmentDto);
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }


}




