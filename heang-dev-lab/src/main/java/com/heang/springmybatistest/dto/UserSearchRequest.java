package com.heang.springmybatistest.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for practicing Dynamic SQL search/filter operations
 * Contains various filter criteria to demonstrate different Dynamic SQL features
 */
@Getter
@Setter
public class UserSearchRequest {

    // For <if> tag practice - simple condition
    private String username;

    // For <if> tag practice - simple condition
    private String email;

    // For <if> tag practice - status filter
    private String status;

    // For <choose><when><otherwise> practice - sorting
    private String sortBy;        // "username", "email", "createdAt"
    private String sortOrder;     // "ASC" or "DESC"

    // For <foreach> practice - multiple IDs
    private List<Long> ids;

    // For <foreach> practice - multiple statuses
    private List<String> statuses;

    // For date range filtering with <if>
    private LocalDate startDate;
    private LocalDate endDate;

    // For <if> with LIKE search
    private String keyword;

    // For pagination
    private Integer page;
    private Integer size;

    // Helper method for offset calculation
    public Integer getOffset() {
        if (page == null || size == null) {
            return null;
        }
        return (page - 1) * size;
    }
}
