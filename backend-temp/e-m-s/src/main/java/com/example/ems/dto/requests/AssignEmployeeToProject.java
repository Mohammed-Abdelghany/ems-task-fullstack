package com.example.ems.dto.requests;

import com.example.ems.entity.ProjectRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignEmployeeToProject(
        @NotNull(message = "Employee ID is required")
        UUID EmployeeId,
        @NotNull(message = "Project ID is required")
        UUID ProjectId,
        @NotNull(message = "Role is required")
        ProjectRole role) {
}
