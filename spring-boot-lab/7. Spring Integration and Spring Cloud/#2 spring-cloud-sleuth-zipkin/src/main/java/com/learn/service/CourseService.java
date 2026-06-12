package com.learn.service;


import com.learn.dto.request.CourseFilterRequest;
import com.learn.dto.request.CourseRequest;
import com.learn.dto.response.CourseResponse;
import com.learn.dto.response.CourseResponsePagination;

import java.util.List;

public interface CourseService {

    void create(CourseRequest courseRequest);
    void update(Long id, CourseRequest courseRequest);
    void delete(Long id);
    CourseResponse getById(Long id);
    List<CourseResponse> getAllCourses();
    CourseResponsePagination filterWithPagination(CourseFilterRequest filterRequest);
}
