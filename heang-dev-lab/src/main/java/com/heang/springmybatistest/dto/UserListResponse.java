package com.heang.springmybatistest.dto;

import java.util.List;

public record UserListResponse(List<UserResponse> users, int total, int page, int size) {
}
