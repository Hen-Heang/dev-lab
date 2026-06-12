package com.learn.service;

import com.learn.dto.ResponseErrorTemplate;
import com.learn.dto.UserRequest;

public interface UserService {

    ResponseErrorTemplate create(UserRequest userRequest);
    ResponseErrorTemplate findById(Long id);
    ResponseErrorTemplate findByUsername(String username);

}
