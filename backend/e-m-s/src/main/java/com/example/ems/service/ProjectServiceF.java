package com.example.ems.service;

import com.example.ems.dto.ProjectDto;
import com.example.ems.dto.requests.CreateProjectRequest;
import com.example.ems.dto.requests.Employee_Role;
import com.example.ems.dto.requests.Project_Department;
import com.example.ems.dto.responses.ProjectWithRole;
import com.example.ems.entity.ProjectRole;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProjectServiceF {
    Page<ProjectDto> getAllProjects(int page, int size);

    ProjectDto createProject(CreateProjectRequest projectRequest);

    ProjectDto updateProject(CreateProjectRequest projectRequest, UUID projectId);

    ProjectDto addEmployeeToProject(UUID projectId, UUID employeeId, ProjectRole role);

    List<ProjectDto> assignMultipleEmployeesToProject(UUID projectId, List<Employee_Role> employees);

    List<ProjectDto> assignDefaultProjectsToDepartments(List<Project_Department> projectDepartments);

    void deleteProject(UUID projectId);

    ProjectDto assignProjectToDepartment(UUID projectId, UUID departmentId);

    List<ProjectDto> findProjectsByDepartment(UUID departmentId);

    List<ProjectWithRole> findProjectsByEmployeeId(UUID employeeId);
}
