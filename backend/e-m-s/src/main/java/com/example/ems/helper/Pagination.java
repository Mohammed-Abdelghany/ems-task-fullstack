package com.example.ems.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
public class Pagination {

    public static Pageable pageable(int page, int size, Sort sort) {
        if (page < 1 || size <= 0) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 1 and size must be greater than 0");
        }
        if (size > 100) {
            throw new IllegalArgumentException("Size must be less than or equal to 100");
        }
        int springPage = page - 1;
        return PageRequest.of(springPage, size, sort);
    }
}
