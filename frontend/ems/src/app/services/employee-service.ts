import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee } from '../model/employee';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private apiUrl = 'http://localhost:8080/api/employees';
  constructor(private http: HttpClient) { }
  getEmployees(page: number, size: number, departmentId?: string): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    if (departmentId && departmentId !== '') {
      return this.http.get<any>(`${this.apiUrl}/department/${departmentId}`, { params });
    } else {
      return this.http.get<any>(this.apiUrl, { params });
    }
  }
  createEmployee(employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(this.apiUrl, employee);
  }
  updateEmployee(id: string, employeeData: any): Observable<Employee> {
    return this.http.put<Employee>(`${this.apiUrl}/update-info/${id}`, employeeData);
  }
  deleteEmployee(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
}
