package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.dto.UserResponse;
import com.heang.springmybatistest.model.Users;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserDtoMapper {

    public UserResponse toUserResponse(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
    public List<UserResponse> toUserResponseList(List<Users> users) {
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }

}
