import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { Employee } from '../../model/employee';
import { Department } from '../../model/department';
import { DepartmentService } from '../../services/department-service';
import {EmployeeService} from '../../services/employee-service';
@Component({
  selector: 'app-employees',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CurrencyPipe],
  templateUrl: './employees.html',
  styleUrl: './employees.css'
})
export class Employees implements OnInit {
  constructor(
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private cdr: ChangeDetectorRef
  ) {}
  employeesList: Employee[] = [];
  departmentsList: Department[] = [];

  employee: Employee = { name: '', email: '', phone: '', hireDate: '', salary: 0, departmentId: '' };
  idEmployee: string | undefined = '';
  message = '';
  errorMessage = '';
  isLoading = false;
  isModalOpen = false;
  isEditMode = false;
  currentPage = 1;
  pageSize = 5;
  totalPages = 1;
  pageNumbers: number[] = [];
  ngOnInit(): void {
    this.loadEmployees();
    this.loadAllDepartments();
  }
  selectedDepartmentFilter = '';
  loadEmployees(): void {
    this.isLoading = true;

    this.employeeService.getEmployees(this.currentPage, this.pageSize, this.selectedDepartmentFilter).subscribe({
      next: (res: any) => {
        const rawEmployees = res.content || res.data || [];

        this.employeesList = rawEmployees.map((emp: Employee) => {
          const dept = this.departmentsList.find(d => d.id === emp.departmentId);
          return {
            ...emp,
            departmentName: dept ? dept.name : 'Unknown Department'
          };
        });
        const totalCount = res.totalElements || res.totalCount || 0;
        this.totalPages = Math.ceil(totalCount / this.pageSize) || 1;
        this.pageNumbers = Array.from({ length: this.totalPages }, (_, i) => i + 1);

        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => { this.isLoading = false; this.cdr.detectChanges(); }
    });
  }
  onFilterChange(): void {
    this.currentPage = 1;
    this.loadEmployees();
  }
  loadAllDepartments(): void {
    this.departmentService.getDepartments(1, 100).subscribe({
      next: (res: any) => {
        this.departmentsList = res.content || res.data || [];
      }
    });
  }
  openModal(): void {
    this.isEditMode = false;
    this.message = '';
    this.errorMessage = '';
    this.employee = { name: '', email: '', phone: '', hireDate: '', salary: 0, departmentId: '' };
    this.isModalOpen = true;
  }
  editEmployee(emp: Employee): void {
    this.isEditMode = true;
    this.message = '';
    this.errorMessage = '';
    this.employee = { ...emp };
    this.isModalOpen = true;
  }
  closeModal(): void {
    this.isModalOpen = false;
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadEmployees();
    }
  }
  saveEmployee(): void {
    this.message = '';
    this.errorMessage = '';
    if (!this.validateEmployee()) return;
    this.isLoading = true;

    if (this.isEditMode) {
      const { id, ...employeeData } = this.employee;
      this.idEmployee = id;
      this.employeeService.updateEmployee(this.idEmployee!, employeeData as any)
        .subscribe({
          next: (res: Employee) => {
            this.isLoading = false;
            this.message = 'Updated successfully';
            this.loadEmployees();
            this.cdr.detectChanges();
          },
          error: (err) => {
            this.isLoading = false;
            this.errorMessage = err?.error?.msg ?? 'Something went wrong';
            this.cdr.detectChanges();
          }
        });
    } else {
      this.employeeService.createEmployee(this.employee)
        .subscribe({
          next: (res: Employee) => {
            this.isLoading = false;
            this.message = 'Created successfully';
            this.currentPage = 1;
            this.employeesList = [res, ...this.employeesList];
            this.loadEmployees();
            this.employee = { name: '', email: '', phone: '', hireDate: '', salary: 0, departmentId: '' };
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
  deleteEmployee(emp: Employee): void {
    if (confirm(`Are you sure you want to delete ${emp.name}?`)) {
      this.employeeService.deleteEmployee(emp.id!).subscribe({
        next: () => {
          alert('Deleted successfully');
          this.loadEmployees();
        }
      });
    }
  }
  private validateEmployee(): boolean {
    if (!this.employee.name?.trim()) { this.errorMessage = 'Name is required'; return false; }
    if (!this.employee.email?.trim()) { this.errorMessage = 'Email is required'; return false; }
    if (!this.employee.departmentId) { this.errorMessage = 'Department is required'; return false; }
    if (this.employee.salary <= 0) { this.errorMessage = 'Salary must be greater than 0'; return false; }
    return true;
  }
}
