package com.example.ems.repo;

import com.example.ems.entity.EmployeeProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeProjectRepo extends JpaRepository<EmployeeProject, UUID> {
}
