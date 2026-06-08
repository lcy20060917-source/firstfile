package com.ecommerce.service;

import com.ecommerce.domain.Product;

import java.util.List;

public interface ProductService {

        Product findById(Long id);

        List<Product> search(String keyword, int page, int size);

        int countSearch(String keyword);

        List<Product> findByCategory(Long categoryId);
}
