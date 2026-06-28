package com.example.ems.dto;

import com.example.ems.entity.Employee;
import com.example.ems.entity.ProjectRole;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ProjectDto(
        UUID id,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        List<UUID> employees,
        List<UUID> departmentIds,
        ProjectRole role
) {
}
