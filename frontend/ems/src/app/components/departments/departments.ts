import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Department } from '../../model/department';
import { DepartmentService } from '../../services/department-service';

@Component({
  selector: 'app-departments',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './departments.html',
  styleUrl: './departments.css'
})
export class Departments implements OnInit {
  constructor(
    private departmentService: DepartmentService,
    private cdr: ChangeDetectorRef
  ) {}

  departmentsList: Department[] = [];
  department: Department = { name: '', budget: 0, location: '' };

  message = '';
  errorMessage = '';
  isLoading = false;
  isModalOpen = false;
  isEditMode = false;
  idDepartment: string | null | undefined ='';
  currentPage = 1;
  pageSize = 5;
  totalPages = 1;
  pageNumbers: number[] = [];

  ngOnInit(): void {
    this.loadDepartments();
  }
  loadDepartments(): void {
    this.isLoading = true;

    this.departmentService.getDepartments(this.currentPage, this.pageSize).subscribe({
      next: (res: any) => {
        this.departmentsList = res.content || res.data || res.items || [];
        const totalCount = res.totalElements || res.totalCount || res.total || 0;

        this.totalPages = Math.ceil(totalCount / this.pageSize) || 1;
        this.pageNumbers = Array.from({ length: this.totalPages }, (_, i) => i + 1);

        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadDepartments();
    }
  }
  saveDepartment(): void {
    this.message = '';
    this.errorMessage = '';
    if (!this.validateDepartment()) return;
    this.isLoading = true;
this.idDepartment=this.department.id;
    if (this.isEditMode) {
      const { id, ...departmentData } = this.department;
      this.idDepartment = id;
      this.departmentService.updateDepartment(this.idDepartment, departmentData as any)
        .subscribe({
          next: (res: Department) => {
            this.isLoading = false;
            this.message = 'Updated successfully';
            this.loadDepartments();
            this.cdr.detectChanges();
          },
          error: (err) => {
            this.isLoading = false;
            this.errorMessage = err?.error?.msg ?? 'Something went wrong';
            this.cdr.detectChanges();
          }
        });
    } else {
      this.departmentService.createDepartment(this.department)
        .subscribe({
          next: (res: Department) => {
            this.isLoading = false;
            this.message = 'Created successfully';
            this.currentPage = 1;
            this.departmentsList = [res, ...this.departmentsList];
            this.loadDepartments();
            this.department.name = '';
            this.department.budget = 0;
            this.department.location = '';
            this.cdr.detectChanges();
          },
          error: (err) => {
            this.isLoading = false;
            this.errorMessage = err?.error?.msg ?? 'Something went wrong';
            this.cdr.detectChanges();
          }
        });
    }
  }

  deleteDepartment(department: Department): void {
    if (confirm(`Are you sure you want to delete ${department.name}?`)) {
      this.departmentService.deleteDepartment(department).subscribe({
        next: () => {
          alert('Deleted successfully');
          this.loadDepartments();
        },
        error: (err) => {
          alert(err?.error?.msg ?? 'Could not delete this department');
        }
      });
    }
  }

  openModal(): void {
    this.isEditMode = false;
    this.message = '';
    this.errorMessage = '';
    this.department = { name: '', budget: 0, location: '' };
    this.isModalOpen = true;
  }

  editDepartment(department: Department): void {
    this.isEditMode = true;
    this.message = '';
    this.errorMessage = '';
    this.department = { ...department };
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  private validateDepartment(): boolean {
    if (!this.department.name?.trim()) {
      this.errorMessage = 'Name is required';
      return false;
    }
    if (this.department.budget <= 0) {
      this.errorMessage = 'Budget must be greater than 0';
      return false;
    }
    if (!this.department.location?.trim()) {
      this.errorMessage = 'Location is required';
      return false;
    }
    return true;
  }
}
