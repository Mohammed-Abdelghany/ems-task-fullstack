package com.example.ems.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateProjectRequest(
        UUID id,
        @NotBlank(message = "Project name is required")
        String name,
        @NotBlank(message = "Project description is required")
        String description,
        @NotNull(message = "Project start date is required")
        LocalDate startDate,
        @NotNull(message = "Project end date is required")
        LocalDate endDate,
        List<UUID> departmentIds

) {
}
