package com.example.ems.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public record CreatEmployeeRequest(
        String name,
        String email,
        String phone,
        LocalDate hireDate,
        BigDecimal salary,
        UUID departmentId
) {

}
