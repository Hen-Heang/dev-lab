package com.heang.springmybatistest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {

    @NotBlank(message = "username is required")
    @Size(max = 100, message = "username must be at most 100 characters")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email format is invalid")
    @Size(max = 150, message = "email must be at most 150 characters")
    private String email;

    @Size(max = 100, message = "name must be at most 100 characters")
    private String name;

    @Size(max = 20, message = "phone must be at most 20 characters")
    private String phone;

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 255, message = "password must be between 8 and 255 characters")
    @jakarta.validation.constraints.Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "password must include at least one letter and one number"
    )
    private String password;

    @Size(max = 20, message = "role must be at most 20 characters")
    private String role;

    @Size(max = 20, message = "status must be at most 20 characters")
    private String status;
}
