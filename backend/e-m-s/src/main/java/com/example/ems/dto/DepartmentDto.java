package com.example.ems.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record DepartmentDto(
        UUID id,
        String name,
        BigDecimal budget,
        String location
) {}
