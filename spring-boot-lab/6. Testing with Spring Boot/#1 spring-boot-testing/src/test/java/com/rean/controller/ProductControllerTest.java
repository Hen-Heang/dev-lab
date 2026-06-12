package com.learn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.exception.ResourceNotFoundException;
import com.learn.model.Product;
import com.learn.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @WebMvcTest — loads ONLY the web layer (controllers, filters, @ControllerAdvice).
 * The service is mocked with @MockBean. No database is involved.
 * Fast: starts in seconds.
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void create_shouldReturn201WithProduct() throws Exception {
        Product product = Product.builder().id(1L).name("Laptop").price(new BigDecimal("999.99")).category("Electronics").build();
        when(productService.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void getById_shouldReturnProduct() throws Exception {
        Product product = Product.builder().id(1L).name("Laptop").price(new BigDecimal("999.99")).category("Electronics").build();
        when(productService.getById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        when(productService.getById(99L)).thenThrow(new ResourceNotFoundException("Product not found with id: 99"));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        List<Product> products = List.of(
                Product.builder().id(1L).name("Laptop").price(new BigDecimal("999.99")).category("Electronics").build(),
                Product.builder().id(2L).name("Phone").price(new BigDecimal("499.99")).category("Electronics").build()
        );
        when(productService.getAll()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
