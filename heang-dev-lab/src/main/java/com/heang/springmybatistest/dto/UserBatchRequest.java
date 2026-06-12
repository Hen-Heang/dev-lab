package com.heang.springmybatistest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO for batch operations practice with Dynamic SQL
 */
@Getter
@Setter
public class UserBatchRequest {

    // For batch insert with <foreach>
    private List<UserRequest> users;

    // For batch update with <foreach>
    private List<Long> ids;
    private String newStatus;
}
