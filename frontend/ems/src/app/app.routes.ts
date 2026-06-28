import { Routes } from '@angular/router';
import { Departments } from './components/departments/departments';
import { Projects } from './components/projects/projects';
import { Employees } from './components/employees/employees';

export const routes: Routes = [
  { path: 'departments', component: Departments },
  { path: 'employees', component: Employees },
  { path: 'projects', component: Projects },
  { path: '**', redirectTo: 'departments' }
];
