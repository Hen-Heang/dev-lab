package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dto.UserRequest;
import com.heang.springmybatistest.dto.UserResponse;
import com.heang.springmybatistest.dto.UserSearchRequest;
import com.heang.springmybatistest.mapper.DynamicSqlPracticeMapper;
import com.heang.springmybatistest.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DynamicSqlPracticeService
 * <p>
 * This service demonstrates how to use Dynamic SQL features in MyBatis.
 * Study the corresponding XML mapper file to understand each SQL pattern.
 */
@Service
@RequiredArgsConstructor
public class DynamicSqlPracticeServiceImpl implements DynamicSqlPracticeService {

    private final DynamicSqlPracticeMapper dynamicSqlPracticeMapper;

    // =========================================================
    // Helper method to convert Users entity to UserResponse DTO
    // =========================================================
    private UserResponse toUserResponse(Users user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private List<UserResponse> toUserResponseList(List<Users> users) {
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // PRACTICE 1: <if> tag usage
    // Try calling this with different combinations of filters
    // =========================================================
    @Override
    public List<UserResponse> searchWithIf(UserSearchRequest request) {
        /*
         * WHAT TO LEARN:
         * - SQL changes based on which fields are non-null
         * - <where> tag handles AND/OR prefix automatically
         *
         * TRY:
         * 1. Call with empty request -> gets all users
         * 2. Call with only username -> filters by username
         * 3. Call with username + status -> filters by both
         */
        List<Users> users = dynamicSqlPracticeMapper.searchUsersWithIf(request);
        return toUserResponseList(users);
    }

    // =========================================================
    // PRACTICE 2: <choose><when><otherwise> usage
    // Try different sortBy values
    // =========================================================
    @Override
    public List<UserResponse> searchWithChoose(UserSearchRequest request) {
        /*
         * WHAT TO LEARN:
         * - Only ONE branch executes (like switch statement)
         * - <otherwise> is the default case
         *
         * TRY:
         * 1. sortBy=null -> sorts by id (default)
         * 2. sortBy="username" -> sorts by username
         * 3. sortBy="createdAt" + sortOrder="ASC" -> sorts by date ascending
         */
        List<Users> users = dynamicSqlPracticeMapper.searchUsersWithChoose(request);
        return toUserResponseList(users);
    }

    // =========================================================
    // PRACTICE 3: <trim> tag (alternative to <where>)
    // =========================================================
    @Override
    public List<UserResponse> searchWithTrim(UserSearchRequest request) {
        /*
         * WHAT TO LEARN:
         * - <trim> gives you control over prefix/suffix
         * - prefixOverrides removes leading AND/OR
         * - suffixOverrides removes trailing commas (useful for SET)
         */
        List<Users> users = dynamicSqlPracticeMapper.searchUsersWithTrim(request);
        return toUserResponseList(users);
    }

    // =========================================================
    // PRACTICE 4: LIKE search with <bind>
    // =========================================================
    @Override
    public List<UserResponse> searchByKeyword(String keyword) {
        /*
         * WHAT TO LEARN:
         * - <bind> creates reusable variables
         * - Useful for LIKE patterns to avoid SQL injection
         */
        List<Users> users = dynamicSqlPracticeMapper.searchByKeyword(keyword);
        return toUserResponseList(users);
    }

    // =========================================================
    // PRACTICE 5: <foreach> with IN clause
    // =========================================================
    @Override
    public List<UserResponse> findByIds(List<Long> ids) {
        /*
         * WHAT TO LEARN:
         * - <foreach> iterates over collections
         * - open="(" close=")" creates parentheses
         * - separator="," adds commas between items
         *
         * GENERATED SQL: SELECT * FROM users WHERE id IN (1, 2, 3)
         */
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<Users> users = dynamicSqlPracticeMapper.findUsersByIds(ids);
        return toUserResponseList(users);
    }

    @Override
    public List<UserResponse> findByStatuses(List<String> statuses) {
        /*
         * SAME PATTERN: <foreach> for multiple values
         * GENERATED SQL: SELECT * FROM users WHERE status IN ('ACTIVE', 'INACTIVE')
         */
        if (statuses == null || statuses.isEmpty()) {
            return List.of();
        }
        List<Users> users = dynamicSqlPracticeMapper.findUsersByStatuses(statuses);
        return toUserResponseList(users);
    }

    // =========================================================
    // PRACTICE 6: <foreach> for Batch INSERT
    // =========================================================
    @Override
    @Transactional
    public int batchInsert(List<UserRequest> users) {
        /*
         * WHAT TO LEARN:
         * - <foreach> can build multiple VALUES clauses
         * - Much faster than individual inserts
         *
         * GENERATED SQL:
         * INSERT INTO users (username, email, status)
         * VALUES ('user1', 'email1', 'ACTIVE'),
         *        ('user2', 'email2', 'ACTIVE'),
         *        ('user3', 'email3', 'INACTIVE')
         */
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return dynamicSqlPracticeMapper.batchInsertUsers(users);
    }

    // =========================================================
    // PRACTICE 7: <foreach> for Batch UPDATE
    // =========================================================
    @Override
    @Transactional
    public int batchUpdateStatus(List<Long> ids, String status) {
        /*
         * GENERATED SQL:
         * UPDATE users SET status = 'INACTIVE' WHERE id IN (1, 2, 3)
         */
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return dynamicSqlPracticeMapper.batchUpdateStatus(ids, status);
    }

    // =========================================================
    // PRACTICE 8: <foreach> for Batch DELETE
    // =========================================================
    @Override
    @Transactional
    public int batchDelete(List<Long> ids) {
        /*
         * GENERATED SQL:
         * DELETE FROM users WHERE id IN (1, 2, 3)
         */
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return dynamicSqlPracticeMapper.batchDeleteUsers(ids);
    }

    // =========================================================
    // PRACTICE 9: <set> for Dynamic UPDATE
    // =========================================================
    @Override
    @Transactional
    public UserResponse dynamicUpdate(Long id, UserRequest request) {
        /*
         * WHAT TO LEARN:
         * - <set> only includes non-null fields
         * - Removes trailing commas automatically
         *
         * IF username="john", email=null, status="ACTIVE":
         * GENERATED SQL:
         * UPDATE users SET username = 'john', status = 'ACTIVE' WHERE id = 1
         *
         * Notice: email is NOT in the SQL because it's null!
         */
        dynamicSqlPracticeMapper.dynamicUpdate(id, request);

        // Return the updated user (would need to fetch from DB in a real scenario)
        // For simplicity, creating response from request
        return UserResponse.builder()
                .id(id)
                .username(request.getUsername())
                .email(request.getEmail())
                .status(request.getStatus())
                .build();
    }

    // =========================================================
    // PRACTICE 10: Advanced Search - EVERYTHING COMBINED!
    // =========================================================
    @Override
    public List<UserResponse> advancedSearch(UserSearchRequest request) {
        /*
         * THIS IS THE MAIN PRACTICE!
         * Combines: <if>, <where>, <foreach>, <choose>, pagination
         *
         * TRY DIFFERENT COMBINATIONS:
         * 1. Empty request -> all users, sorted by id DESC
         * 2. keyword="john" -> searches in username AND email
         * 3. statuses=[ "ACTIVE","PENDING"] -> users with these statuses
         * 4. sortBy="username", sortOrder="ASC" -> custom sorting
         * 5. page=1, size=10 -> first 10 results
         * 6. page=2, size=10 -> next 10 results
         */
        List<Users> users = dynamicSqlPracticeMapper.advancedSearch(request);
        return toUserResponseList(users);
    }

    @Override
    public int countAdvancedSearch(UserSearchRequest request) {
        /*
         * For pagination - get total count
         * Use with advancedSearch to calculate total pages
         */
        return dynamicSqlPracticeMapper.countAdvancedSearch(request);
    }
}
