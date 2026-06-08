package com.ecommerce.repository.impl;

import com.ecommerce.domain.CartItem;
import com.ecommerce.repository.CartRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CartRepositoryImpl implements CartRepository {

    private final SqlSessionTemplate sqlSession;

    public CartRepositoryImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    private static final String NAMESPACE = "com.ecommerce.repository.CartRepository.";

    @Override
    public List<CartItem> findByUserId(Long userId) {
        return sqlSession.selectList(NAMESPACE + "findByUserId", userId);
    }

    @Override
    public CartItem findByUserIdAndProductId(Long userId, Long productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("productId", productId);
        return sqlSession.selectOne(NAMESPACE + "findByUserIdAndProductId", params);
    }

    @Override
    public int insert(CartItem cartItem) {
        return sqlSession.insert(NAMESPACE + "insert", cartItem);
    }

    @Override
    public int updateQuantity(CartItem cartItem) {
        return sqlSession.update(NAMESPACE + "updateQuantity", cartItem);
    }

    @Override
    public int deleteById(Long id) {
        return sqlSession.delete(NAMESPACE + "deleteById", id);
    }

    @Override
    public int deleteByUserId(Long userId) {
        return sqlSession.delete(NAMESPACE + "deleteByUserId", userId);
    }

    @Override
    public int deleteByUserIdAndProductId(Long userId, Long productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("productId", productId);
        return sqlSession.delete(NAMESPACE + "deleteByUserIdAndProductId", params);
    }
}
