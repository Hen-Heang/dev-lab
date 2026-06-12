package com.learn.service;

import com.learn.model.Todo;

public interface TodoService {

    Todo saveOrUpdate(Todo todo);
    Todo update(Long id);
    void delete(Long id);
    Todo findById(Long id);
    Iterable<Todo> findAll();
}
