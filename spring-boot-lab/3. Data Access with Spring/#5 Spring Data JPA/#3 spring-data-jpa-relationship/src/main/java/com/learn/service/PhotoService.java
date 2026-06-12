package com.learn.service;

import com.learn.dto.PhotoRequestDto;
import com.learn.model.Photo;

public interface PhotoService {

    void addNewPhoto(PhotoRequestDto photoRequestDto) throws Exception;

    Photo checkById(Long id);
}
