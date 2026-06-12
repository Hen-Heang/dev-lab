package com.learn.service;

import com.learn.model.Product;

import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product getById(Long id);
    List<Product> getAll();
    List<Product> getByCategory(String category);
    Product update(Long id, Product product);
    void delete(Long id);
}
