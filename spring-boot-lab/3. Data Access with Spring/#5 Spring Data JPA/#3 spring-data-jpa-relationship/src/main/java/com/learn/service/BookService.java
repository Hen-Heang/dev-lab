package com.learn.service;

import com.learn.dto.BookRequestDto;
import com.learn.dto.BookResponseDto;

import java.util.List;

public interface BookService {

    void createNewBook(BookRequestDto bookRequestDto) throws Exception;

    List<BookResponseDto> listBooks() throws Exception;

}
