package com.example.ems.dto.responses;

import com.example.ems.entity.Project;
import com.example.ems.entity.ProjectRole;

import java.time.LocalDate;

public record ProjectWithRole (ProjectRole role, String name,LocalDate startDate, LocalDate endDate, String description ) {
}
