import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AssignEmployeeRequest } from '../model/AssignEmployeeRequest';
import { Project } from '../model/project';

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private apiUrl = 'http://localhost:8080/api/projects';



  constructor(private http: HttpClient) {}

  getProjects(page: number, size: number): Observable<any> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<any>(this.apiUrl, { params });
  }

  createProject(project: any): Observable<Project> {
    return this.http.post<Project>(this.apiUrl, project);
  }

  assignEmployee(assignment: AssignEmployeeRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/add-employee`, assignment);
  }

  updateProject(id: string, project: any): Observable<Project> {
    return this.http.put<Project>(`${this.apiUrl}/${id}`, project);
  }

  deleteProject(id: string): Observable<any> {
    return this.http.delete<any>(`${`${this.apiUrl}/${id}`}`);
  }
}
