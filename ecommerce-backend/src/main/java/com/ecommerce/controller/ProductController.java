package com.ecommerce.controller;

import com.ecommerce.common.Result;
import com.ecommerce.domain.Product;
import com.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long category) {

        log.debug("商品搜索: keyword={}, page={}, size={}, category={}", keyword, page, size, category);

        List<Product> products;
        int total;
        if (category != null) {
            products = productService.findByCategory(category);
            total = products.size();
            int start = page * size;
            int end = Math.min(start + size, products.size());
            products = start < products.size() ? products.subList(start, end) : products;
        } else {
            products = productService.search(keyword, page, size);
            total = productService.countSearch(keyword);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("content", products);
        data.put("totalElements", total);
        data.put("totalPages", (int) Math.ceil((double) total / size));
        data.put("size", size);
        data.put("number", page);

        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        log.debug("商品详情: id={}", id);
        Product product = productService.findById(id);
        return Result.success(product);
    }
}
