package com.ecommerce.service.impl;

import com.ecommerce.common.Constants;
import com.ecommerce.domain.Product;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findById(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        if (product.getStatus() != Constants.ProductStatus.ON_SALE) {
            throw new BusinessException(400, "商品已下架");
        }
        return product;
    }

    @Override
    public List<Product> search(String keyword, int page, int size) {
        int offset = page * size;
        int limit = Math.min(size, 100); // 限制每页最大 100 条
        log.debug("搜索商品: keyword={}, page={}, size={}", keyword, page, size);
        return productRepository.search(keyword, offset, limit);
    }

    @Override
    public int countSearch(String keyword) {
        return productRepository.countSearch(keyword);
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}
