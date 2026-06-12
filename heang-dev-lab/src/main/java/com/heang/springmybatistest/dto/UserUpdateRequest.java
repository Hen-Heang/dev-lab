package com.heang.springmybatistest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UserUpdateRequest {

    @NotBlank(message = "username is required")
    @Size(max = 100, message = "username must be at most 100 characters")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email format is invalid")
    @Size(max = 150, message = "email must be at most 150 characters")
    private String email;

    @Size(max = 20, message = "status must be at most 20 characters")
    private String status;

}
