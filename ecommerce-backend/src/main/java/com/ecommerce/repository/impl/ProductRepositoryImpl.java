package com.ecommerce.repository.impl;

import com.ecommerce.domain.Product;
import com.ecommerce.repository.ProductRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final SqlSessionTemplate sqlSession;

    public ProductRepositoryImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    private static final String NAMESPACE = "com.ecommerce.repository.ProductRepository.";

    @Override
    public Product findById(Long id) {
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    @Override
    public List<Product> search(String keyword, int offset, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);
        return sqlSession.selectList(NAMESPACE + "search", params);
    }

    @Override
    public int countSearch(String keyword) {
        return sqlSession.selectOne(NAMESPACE + "countSearch", keyword);
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return sqlSession.selectList(NAMESPACE + "findByCategoryId", categoryId);
    }

    @Override
    public int insert(Product product) {
        return sqlSession.insert(NAMESPACE + "insert", product);
    }

    @Override
    public int update(Product product) {
        return sqlSession.update(NAMESPACE + "update", product);
    }

    @Override
    public int updateStock(Long id, Integer stock) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("stock", stock);
        return sqlSession.update(NAMESPACE + "updateStock", params);
    }

    @Override
    public int deleteById(Long id) {
        return sqlSession.delete(NAMESPACE + "deleteById", id);
    }
}
