package com.heang.springmybatistest.model;


import lombok.*;

import java.time.LocalDateTime;

/**
 * Users Entity (사용자 엔티티)
 * Spring Security UserDetails 연동용
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    private Long id;

    private String username;

    private String email;

    private String password;        // Bcrypt 암호화된 비밀번호

    private String name;            // 사용자 이름

    private String phone;           // 전화번호

    private String role;            // 권한: ROLE_USER, ROLE_ADMIN

    private String status;          // 상태: ACTIVE, INACTIVE, SUSPENDED

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
