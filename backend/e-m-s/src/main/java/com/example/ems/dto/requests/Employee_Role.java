package com.example.ems.dto.requests;

import com.example.ems.entity.ProjectRole;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record Employee_Role(
        @NotNull(message = "Employee ID is required")
        UUID employeeId,
        @NotNull(message = "Role is required")
        ProjectRole role
) {
}
