package com.example.ems.mapper;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.dto.requests.CreatEmployeeRequest;
import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import com.example.ems.entity.EmployeeProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface EmployeeMapper {
    @Mapping(target = "department.id", source = "departmentId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeProjects", ignore = true)
    Employee toEmployee(CreatEmployeeRequest employee);
    @Mapping(target = "departmentId", source = "department.id")
    EmployeeDto toEmployeeDto(Employee employee);

    List<EmployeeDto> toEmployeeDtoList(List<Employee> employees);
    default List<UUID> mapDepartmentsToIds(List<Department> departments) {
        return departments.stream()
                .map(Department::getId)
                .collect(Collectors.toList());
    }



}
