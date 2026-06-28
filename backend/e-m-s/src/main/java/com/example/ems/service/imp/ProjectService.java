package com.example.ems.service.imp;

import com.example.ems.dto.ProjectDto;
import com.example.ems.dto.requests.CreateProjectRequest;
import com.example.ems.dto.requests.Employee_Role;
import com.example.ems.dto.requests.Project_Department;
import com.example.ems.dto.responses.ProjectWithRole;
import com.example.ems.entity.*;
import com.example.ems.helper.Pagination;
import com.example.ems.mapper.ProjectMapper;
import com.example.ems.repo.DepartmentRepo;
import com.example.ems.repo.EmployeeRepo;
import com.example.ems.repo.ProjectRepo;
import com.example.ems.service.ProjectServiceF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService implements ProjectServiceF {
    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    @Transactional(readOnly = true)

    public Page<ProjectDto> getAllProjects(int page, int size) {
     return  projectRepo.findAll(Pagination.pageable(page, size, Sort.by("id").descending()))
                .map(projectMapper::toProjectDto);
    }

    @Transactional
    public ProjectDto createProject(CreateProjectRequest projectRequest) {
        validateProject(projectRequest);

        Project project = projectMapper.toProject(projectRequest);
        List<Department> departments = new ArrayList<>();
        projectRequest.departmentIds().forEach(id -> {
            Department department= departmentRepo.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
            departments.add(department);
        });
        project.setDepartments(departments);
        Project savedProject = projectRepo.save(project);
        return projectMapper.toProjectDto(savedProject);
    }
    @Transactional
    public ProjectDto updateProject(CreateProjectRequest projectRequest, UUID projectId) {
        Project existingProject = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        if (projectRequest.endDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Project end date should not be in the past");
        }
        List<Department> departments = new ArrayList<>();
        projectRequest.departmentIds().forEach(id -> {
            Department department= departmentRepo.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
            departments.add(department);
        });
        existingProject.setDepartments(departments);
        existingProject.setName(projectRequest.name());
        existingProject.setDescription(projectRequest.description());
        existingProject.setStartDate(projectRequest.startDate());
        existingProject.setEndDate(projectRequest.endDate());
        Project updatedProject = projectRepo.save(existingProject);
        return projectMapper.toProjectDto(updatedProject);

    }
    public ProjectDto addEmployeeToProject(UUID projectId, UUID employeeId, ProjectRole role) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        if (role == null) {
            throw new RuntimeException("Role must be specified");
        }
        if (project.getEmployees().stream().anyMatch(ep -> ep.getEmployee().getId().equals(employeeId))) {
            throw new RuntimeException("Employee is already assigned to this project");
        }
        EmployeeProject employeeProject = new EmployeeProject();
        employeeProject.setProject(project);
        employeeProject.setEmployee(employee);
        employeeProject.setRole(role);
        project.getEmployees().add(employeeProject);
        projectRepo.save(project);
        return projectMapper.toProjectDto(project);
    }
@Transactional
    public List<ProjectDto> assignMultipleEmployeesToProject(UUID projectId, List<Employee_Role> employees) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        for (Employee_Role employee : employees) {
            if (employeeRepo.findById(employee.employeeId()).isEmpty()) {
                throw new RuntimeException("Employee not found");
            }
            if (project.getEmployees().stream().anyMatch(ep -> ep.getEmployee().getId().equals(employee.employeeId()))) {
                throw new RuntimeException("Employee is already assigned to this project");
            }
            if (employee.role() == null) {
                throw new RuntimeException("Role must be specified");
            }
            EmployeeProject employeeProject = new EmployeeProject();
            employeeProject.setProject(project);
            employeeProject.setEmployee(employeeRepo.findById(employee.employeeId()).get());
            employeeProject.setRole(employee.role());
            project.getEmployees().add(employeeProject);
            projectRepo.save(project);
        }
        return projectMapper.toProjectDtoList(List.of(project));
    }
    @Transactional
    public List<ProjectDto> assignDefaultProjectsToDepartments(List<Project_Department> projectDepartments) {
        for (Project_Department projectDepartment : projectDepartments) {
            Project project = projectRepo.findById(projectDepartment.projectId()).orElseThrow(
                    () -> new RuntimeException("Project not found")
            );
            Department department = departmentRepo.findById(projectDepartment.departmentId()).orElseThrow(
                    () -> new RuntimeException("Department not found")
            );
            if (project.getDepartments().contains(department)) {
                throw new RuntimeException("Project is already assigned to this department");
            }
            project.getDepartments().add(department);
            projectRepo.save(project);
        }
            return projectMapper.toProjectDtoList(projectRepo.findAll());

    }
    @Transactional
    public void deleteProject(UUID projectId) {
    if (!projectRepo.existsById(projectId)) {
        throw new RuntimeException("Project not found");
    }
    projectRepo.deleteById(projectId);
    }
    private void validateProject(CreateProjectRequest projectRequest) {
      if (projectRequest.startDate() != null && projectRequest.endDate() != null) {
            if (projectRequest.startDate().isAfter(projectRequest.endDate())) {
                throw new RuntimeException("Project start date should not be after end date");
            }
            if (projectRequest.endDate().isBefore(LocalDate.now())) {
                throw new RuntimeException("Project end date should not be in the past");
            }
        }
    }
    @Transactional
    public ProjectDto assignProjectToDepartment(UUID projectId, UUID departmentId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        project.getDepartments().add(department);
        projectRepo.save(project);
        return projectMapper.toProjectDto(project );
    }
    public List<ProjectDto> findProjectsByDepartment(UUID departmentId) {
        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        List<Project> projects = projectRepo.findProjectsByDepartment(department);
        return projectMapper.toProjectDtoList(projects);
    }
    @Transactional(readOnly = true)
    public List<ProjectWithRole> findProjectsByEmployeeId(UUID employeeId) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    return projectRepo.findByEmployee(employee);
    }


}
