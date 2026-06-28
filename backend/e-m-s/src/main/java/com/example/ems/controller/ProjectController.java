package com.example.ems.controller;
import com.example.ems.dto.ProjectDto;
import com.example.ems.dto.requests.AssignEmployeeToProject;
import com.example.ems.dto.requests.CreateProjectRequest;
import com.example.ems.dto.requests.Employee_Role;
import com.example.ems.dto.requests.Project_Department;
import com.example.ems.dto.responses.ProjectWithRole;
import com.example.ems.entity.Project;
import com.example.ems.mapper.ProjectMapper;
import com.example.ems.service.imp.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @GetMapping
    public ResponseEntity<Page<ProjectDto>> getAllProjects(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getAllProjects(page, size));
    }
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody @Valid CreateProjectRequest createProjectRequest) {
        return ResponseEntity.ok(projectService.createProject(createProjectRequest));
    }
    @GetMapping("/department")
    public ResponseEntity<List<ProjectDto>> findProjectsDepartment(@RequestParam UUID departmentId) {
        return ResponseEntity.ok(projectService.findProjectsByDepartment(departmentId));
    }
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(@RequestBody @Valid CreateProjectRequest createProjectRequest, @PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.updateProject(createProjectRequest, projectId));
    }
    @PostMapping("/add-employee")
    public ResponseEntity<ProjectDto> addEmployeeToProject(@RequestBody @Valid AssignEmployeeToProject assignEmployeeToProject) {
        ProjectDto projectDto= projectService.addEmployeeToProject(assignEmployeeToProject.ProjectId(), assignEmployeeToProject.EmployeeId(), assignEmployeeToProject.role());
        return ResponseEntity.ok(projectDto);
    }

 @PostMapping("/add-employees/{projectId}")
    public ResponseEntity< List<ProjectDto>> addEmployeesToProject(@PathVariable UUID projectId, @RequestBody List<Employee_Role> assignEmployeesToProjects) {
      List<ProjectDto>  projectDto  = projectService.assignMultipleEmployeesToProject(projectId, assignEmployeesToProjects);
        return ResponseEntity.ok(projectDto);
    }

    @PostMapping("/{projectId}/department/{departmentId}")
    public ResponseEntity<ProjectDto> addDepartmentToProject(@PathVariable UUID projectId, @PathVariable UUID departmentId) {
        ProjectDto projectDto = projectService.assignProjectToDepartment(projectId, departmentId);
        return ResponseEntity.ok(projectDto);
    }
    @PostMapping("/default-projects")
    public ResponseEntity<List<ProjectDto>> addDefProjectsToDiffDepartments (@RequestBody List<Project_Department> projectDepartments) {
        List<ProjectDto> projectDtos = projectService.assignDefaultProjectsToDepartments(projectDepartments);
        return ResponseEntity.ok(projectDtos);
    }
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ProjectWithRole>> findProjectsByEmployeeId(@PathVariable UUID employeeId) {
        List<ProjectWithRole> projectDtos = projectService.findProjectsByEmployeeId(employeeId);
        return ResponseEntity.ok (projectDtos);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

}
