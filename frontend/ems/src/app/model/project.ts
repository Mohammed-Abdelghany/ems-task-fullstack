export interface Project {
  id?: string;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  employees?: string[];
  departmentIds?: string[];
}
