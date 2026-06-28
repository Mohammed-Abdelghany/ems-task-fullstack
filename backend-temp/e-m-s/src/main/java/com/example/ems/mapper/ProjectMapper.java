package com.example.ems.mapper;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.dto.ProjectDto;
import com.example.ems.dto.requests.CreateProjectRequest;
import com.example.ems.entity.EmployeeProject;
import com.example.ems.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",uses = {EmployeeMapper.class})
public interface ProjectMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Project toProject(CreateProjectRequest projectRequest);
    @Mapping(target = "employees", source = "employees")
    @Mapping(target = "departmentIds", source = "departments")
    ProjectDto toProjectDto(Project project);
    List<ProjectDto> toProjectDtoList(List<Project> projects);
    List<Project> toProjectList(List<CreateProjectRequest> projectRequests);
    default UUID toEmployeeId(EmployeeProject employeeProject) {
        return employeeProject.getEmployee().getId();
    }
}
