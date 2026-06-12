package com.learn.service;

import com.learn.dto.AuthorRequestDto;
import com.learn.model.Author;

public interface AuthorService {

    void add(AuthorRequestDto authorRequestDto) throws Exception;

    Author findById(Long id) throws Exception;
}
