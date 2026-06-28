import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Department} from '../model/department';

@Injectable({
  providedIn: 'root',
})
export class DepartmentService {
  private apiUrl = 'http://localhost:8080/api/departments';

  constructor(private http: HttpClient) { }

  getDepartments(page: number, size: number): Observable<any> {
      const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(this.apiUrl, { params });
  }
  createDepartment(department: Department): Observable<Department> {
    return this.http.post<Department>(this.apiUrl, department);
  }

  addDepartment(department: any): Observable<any> {
    return this.http.post(this.apiUrl, department);
  }

  deleteDepartment(department:Department): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${department.id}`);
  }

  updateDepartment(id: any, department: Department): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, department);
  }
}
