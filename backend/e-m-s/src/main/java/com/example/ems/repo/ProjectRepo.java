package com.example.ems.repo;

import com.example.ems.dto.responses.ProjectWithRole;
import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import com.example.ems.entity.EmployeeProject;
import com.example.ems.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepo extends JpaRepository<Project, UUID> {
    @Query("select p from Project p join p.departments d where d = :department")
    List<Project> findProjectsByDepartment(@Param("department") Department department);

    @Query("""
    select new com.example.ems.dto.responses.ProjectWithRole( ep.role, p.name,p.startDate,p.endDate,p.description)  from Project p   join p.employees ep where ep.employee = :employee  """)
    List<ProjectWithRole> findByEmployee(@Param("employee") Employee employee);
}


