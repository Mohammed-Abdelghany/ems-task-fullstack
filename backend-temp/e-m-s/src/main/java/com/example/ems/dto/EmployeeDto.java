package com.example.ems.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public record EmployeeDto(
        UUID id,
        String name,
        String email,
        String phone,
        LocalDate hireDate,
        BigDecimal salary,
        UUID departmentId
) {

}
