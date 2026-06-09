package com.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    Product findById(Long id);
    IPage<Product> search(String keyword, int page, int size);
    List<Product> findByCategory(Long categoryId);
}
