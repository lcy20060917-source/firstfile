package com.ecommerce.repository;

import com.ecommerce.domain.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductRepository {

        Product findById(Long id);

        List<Product> search(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

        int countSearch(@Param("keyword") String keyword);

        List<Product> findByCategoryId(Long categoryId);

        int insert(Product product);

        int update(Product product);

        int updateStock(@Param("id") Long id, @Param("stock") Integer stock);

        int deleteById(Long id);
}
