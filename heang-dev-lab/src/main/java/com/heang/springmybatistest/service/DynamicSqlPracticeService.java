package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dto.UserRequest;
import com.heang.springmybatistest.dto.UserResponse;
import com.heang.springmybatistest.dto.UserSearchRequest;

import java.util.List;

/**
 * Practice Service Interface for Dynamic SQL operations
 */
public interface DynamicSqlPracticeService {

    // Search operations
    List<UserResponse> searchWithIf(UserSearchRequest request);
    List<UserResponse> searchWithChoose(UserSearchRequest request);
    List<UserResponse> searchWithTrim(UserSearchRequest request);
    List<UserResponse> searchByKeyword(String keyword);

    // Foreach operations
    List<UserResponse> findByIds(List<Long> ids);
    List<UserResponse> findByStatuses(List<String> statuses);

    // Batch operations
    int batchInsert(List<UserRequest> users);
    int batchUpdateStatus(List<Long> ids, String status);
    int batchDelete(List<Long> ids);

    // Dynamic update
    UserResponse dynamicUpdate(Long id, UserRequest request);

    // Advanced search with pagination
    List<UserResponse> advancedSearch(UserSearchRequest request);
    int countAdvancedSearch(UserSearchRequest request);
}
