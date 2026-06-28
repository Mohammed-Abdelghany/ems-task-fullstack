package com.example.ems.dto.requests;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.math.BigDecimal;
import java.util.UUID;


public record CreateDepartmentRequest(
        @Null(message = "Department ID must be null for creation")
        UUID id,
        @NotBlank(message = "Department name is required")
        String name,
        @NotNull(message = "Budget is required")
        @DecimalMin(value = "0.0",inclusive = false, message = "Budget must be greater than 0")
        BigDecimal budget,
        @NotBlank(message = "Department location is required")
        String location

) {
}
