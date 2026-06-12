package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.dto.UserRequest;
import com.heang.springmybatistest.dto.UserSearchRequest;
import com.heang.springmybatistest.dto.UserUpdateRequest;
import com.heang.springmybatistest.model.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * User Mapper Interface (사용자 매퍼 인터페이스)
 * Spring Security 연동 포함
 */
@Mapper
public interface UserMapper {

    // 기존 CRUD 메서드
    void insertUser(UserRequest userRequest);

    List<Users> selectUserList();

    void updateUser(@Param("id") Long id,
                    @Param("req") UserUpdateRequest userRequest);

    Users selectUserById(Long id);

    void deleteUser(Long id);

    List<Users> searchUsers(UserSearchRequest request);

    int countUsers(UserSearchRequest request);

    // ===== Spring Security 관련 메서드 =====

    /**
     * 사용자명으로 사용자 조회 (로그인용)
     * Spring Security UserDetailsService에서 사용
     */
    Users selectUserByUsername(String username);

    /**
     * 이메일로 사용자 조회
     */
    Users selectUserByEmail(String email);

    /**
     * 사용자명 중복 확인
     */
    int countByUsername(String username);

    /**
     * 이메일 중복 확인
     */
    int countByEmail(String email);

    /**
     * 회원가입 (비밀번호 포함)
     */
    void insertUserWithPassword(Users user);

    /**
     * 비밀번호 변경
     */
    void updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 마지막 로그인 시간 업데이트
     */
    void updateLastLogin(Long id);
}
