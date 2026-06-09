package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public Product findById(Long id) {
        Product p = productMapper.selectById(id);
        if (p == null) throw new BusinessException(404, "商品不存在");
        return p;
    }

    @Override
    public IPage<Product> search(String keyword, int page, int size) {
        LambdaQueryWrapper<Product> w = new LambdaQueryWrapper<>();
        w.eq(Product::getStatus, 1);
        if (keyword != null && !keyword.isEmpty()) {
            w.like(Product::getName, keyword);
        }
        w.orderByDesc(Product::getCreateTime);
        return productMapper.selectPage(new Page<>(page + 1, size), w);
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productMapper.selectList(
                new LambdaQueryWrapper<Product>()
                        .eq(Product::getCategoryId, categoryId)
                        .eq(Product::getStatus, 1));
    }
}
