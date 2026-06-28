package com.example.ems.mapper;

import com.example.ems.dto.DepartmentDto;
import com.example.ems.dto.requests.CreateDepartmentRequest;
import com.example.ems.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Department toDepartment(CreateDepartmentRequest createDepartmentRequest);
    DepartmentDto toDepartmentDto(Department department);





}
