import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Department } from '../../model/department';
import { Employee } from '../../model/employee';
import { ProjectService } from '../../services/project-service';
import { DepartmentService } from '../../services/department-service';
import { EmployeeService } from '../../services/employee-service';
import { Project } from '../../model/project';
import { AssignEmployeeRequest } from '../../model/AssignEmployeeRequest';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './projects.html',
  styleUrl: './projects.css'
})
export class Projects implements OnInit {
  projectsList: Project[] = [];
  departmentsList: Department[] = [];
  allEmployeesList: Employee[] = [];

  project: Project = { name: '', description: '', startDate: '', endDate: '', departmentIds: [] };

  selectedProjectId = '';
  newAssignment = { employeeId: '', role: '' };

  isLoading = false;
  isProjectModalOpen = false;
  isAssignModalOpen = false;
  isEditMode = false;
  message = '';
  errorMessage = '';

  currentPage = 1;
  pageSize = 5;
  totalPages = 1;
  pageNumbers: number[] = [];

  constructor(
    private projectService: ProjectService,
    private departmentService: DepartmentService,
    private employeeService: EmployeeService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAllEmployees();
  }

  loadProjects(): void {
    this.isLoading = true;
    this.projectService.getProjects(this.currentPage, this.pageSize).subscribe({
      next: (res: any) => {
        console.log('Data from Backend:', res);

        let rawProjects = [];
        if (Array.isArray(res)) {
          rawProjects = res;
        } else {
          rawProjects = res.content || res.data || [];
        }

        this.projectsList = rawProjects.map((proj: Project) => ({
          ...proj
        }));

        this.totalPages = res.totalPages || 1;
        this.pageNumbers = Array.from({ length: this.totalPages }, (_, i) => i + 1);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err?.error?.message ?? 'Error loading projects';
        this.cdr.detectChanges();
      }
    });
  }

  loadAllDepartments(): void {
    this.departmentService.getDepartments(1, 100).subscribe({
      next: (res: any) => {
        this.departmentsList = res.content || [];
        this.loadProjects();
        this.cdr.detectChanges();
      },
      error: () => {
        this.loadProjects();
        this.cdr.detectChanges();
      }
    });
  }

  loadAllEmployees(): void {
    this.employeeService.getEmployees(1, 100).subscribe({
      next: (res: any) => {
        this.allEmployeesList = res.content || [];
        this.loadAllDepartments();
        this.cdr.detectChanges();
      },
      error: () => {
        this.loadAllDepartments();
      }
    });
  }

  openProjectModal(): void {
    this.message = '';
    this.errorMessage = '';
    this.isEditMode = false;
    this.project = { name: '', description: '', startDate: '', endDate: '', departmentIds: [] };
    this.isProjectModalOpen = true;
  }

  openEditModal(proj: Project): void {
    this.message = '';
    this.errorMessage = '';
    this.isEditMode = true;

    this.project = {
      id: proj.id,
      name: proj.name,
      description: proj.description,
      startDate: proj.startDate,
      endDate: proj.endDate,
      departmentIds: proj.departmentIds ? [...proj.departmentIds] : []
    };
    this.isProjectModalOpen = true;
    this.cdr.detectChanges();
  }

  saveProject(): void {
    this.message = '';
    this.errorMessage = '';
    if (!this.validateProject()) return;
    this.project.departmentIds = this.project.departmentIds || [];
    this.isLoading = true;

    if (this.isEditMode) {
      this.projectService.updateProject(this.project.id!, this.project).subscribe({
        next: () => {
          this.isLoading = false;
          this.isProjectModalOpen = false;
          this.loadProjects();
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = err?.error?.message ?? 'Failed to update project';
          this.cdr.detectChanges();
        }
      });
    } else {
      this.projectService.createProject(this.project).subscribe({
        next: (res: Project) => {
          this.isLoading = false;
          this.isProjectModalOpen = false;
          this.currentPage = 1;
          this.loadProjects();
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = err?.error?.message ?? 'Failed to create project';
          this.cdr.detectChanges();
        }
      });
    }
  }

  deleteProject(projectId: string): void {
    if (confirm('Are you sure you want to delete this project?')) {
      this.isLoading = true;
      this.projectService.deleteProject(projectId).subscribe({
        next: () => {
          this.loadProjects();
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = err?.error?.message ?? 'Failed to delete project';
          this.cdr.detectChanges();
        }
      });
    }
  }

  openAssignModal(projectId: string): void {
    this.selectedProjectId = projectId;
    this.newAssignment = { employeeId: '', role: '' };
    this.message = '';
    this.errorMessage = '';
    this.isAssignModalOpen = true;
  }

  submitAssignment(): void {
    this.message = '';
    this.errorMessage = '';
    if (!this.newAssignment.employeeId || !this.newAssignment.role) {
      this.errorMessage = 'Please select an employee and a role.';
      return;
    }
    this.isLoading = true;

    const requestData: AssignEmployeeRequest = {
      EmployeeId: this.newAssignment.employeeId,
      ProjectId: this.selectedProjectId,
      role: this.newAssignment.role
    };

    this.projectService.assignEmployee(requestData).subscribe({
      next: () => {
        this.isLoading = false;
        this.message = 'Employee assigned to project successfully!';
        this.loadProjects();
        setTimeout(() => {
          this.isAssignModalOpen = false;
          this.cdr.detectChanges();
        }, 1200);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err?.error?.message ?? 'Error while assigning employee';
        this.cdr.detectChanges();
      }
    });
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadProjects();
    }
  }

  isDepartmentSelected(departmentId: string): boolean {
    return this.project.departmentIds?.includes(departmentId) ?? false;
  }

  toggleDepartmentSelection(departmentId: string): void {
    if (!this.project.departmentIds) {
      this.project.departmentIds = [];
    }

    const index = this.project.departmentIds.indexOf(departmentId);
    if (index > -1) {
      this.project.departmentIds.splice(index, 1);
    } else {
      this.project.departmentIds.push(departmentId);
    }
  }

  getProjectDepartments(departmentIds: string[] | undefined): string[] {
    if (!departmentIds || departmentIds.length === 0) {
      return ['No Departments'];
    }
    if (this.departmentsList.length === 0) {
      return [];
    }
    return departmentIds.map(id => {
      const dept = this.departmentsList.find(d => d.id === id);
      return dept ? dept.name : 'Unknown Department';
    });
  }

  getProjectEmployees(employeeIds: string[] | undefined): string[] {
    if (!employeeIds || employeeIds.length === 0) {
      return [];
    }
    if (this.allEmployeesList.length === 0) {
      return [];
    }
    return employeeIds.map(id => {
      const emp = this.allEmployeesList.find(e => e.id === id);
      return emp ? emp.name : 'Unknown Employee';
    });
  }

  private validateProject(): boolean {
    if (!this.project.name?.trim()) { this.errorMessage = 'Project name is required'; return false; }
    if (!this.project.description?.trim()) { this.errorMessage = 'Description is required'; return false; }
    if (!this.project.startDate) { this.errorMessage = 'Start date is required'; return false; }
    if (!this.project.endDate) { this.errorMessage = 'End date is required'; return false; }
    return true;
  }
}
