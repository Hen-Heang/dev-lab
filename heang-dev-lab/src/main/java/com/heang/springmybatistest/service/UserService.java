package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dto.UserRequest;
import com.heang.springmybatistest.dto.UserListResponse;
import com.heang.springmybatistest.dto.UserResponse;
import com.heang.springmybatistest.dto.UserSearchRequest;
import com.heang.springmybatistest.dto.UserUpdateRequest;
import com.heang.springmybatistest.model.Users;
import jakarta.validation.Valid;

public interface UserService {
    void createUser(@Valid UserRequest userRequest);

    UserListResponse searchUsers(UserSearchRequest request);

    Users selectUserById(Long id);

    UserResponse updateUser(Long id , UserUpdateRequest userUpdateRequest);


    UserResponse getUserById(Long id);

    void deleteUser(Long id);
}
