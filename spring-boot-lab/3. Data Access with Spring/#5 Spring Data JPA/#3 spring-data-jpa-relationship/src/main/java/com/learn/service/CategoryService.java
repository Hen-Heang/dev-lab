package com.learn.service;

import com.learn.dto.CategoryRequestDto;
import com.learn.model.Category;

public interface CategoryService {

    void createNewCategory(CategoryRequestDto categoryRequestDto) throws Exception;

    Category checkById(Long id) throws Exception;
}
