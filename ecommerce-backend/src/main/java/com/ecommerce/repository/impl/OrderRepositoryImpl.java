package com.ecommerce.repository.impl;

import com.ecommerce.domain.Order;
import com.ecommerce.domain.OrderItem;
import com.ecommerce.repository.OrderRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SqlSessionTemplate sqlSession;

    public OrderRepositoryImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    private static final String NAMESPACE = "com.ecommerce.repository.OrderRepository.";

    @Override
    public int insertOrder(Order order) {
        return sqlSession.insert(NAMESPACE + "insertOrder", order);
    }

    @Override
    public int insertOrderItems(List<OrderItem> items) {
        return sqlSession.insert(NAMESPACE + "insertOrderItems", items);
    }

    @Override
    public List<Order> findByUserId(Long userId, int offset, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("limit", limit);
        return sqlSession.selectList(NAMESPACE + "findByUserId", params);
    }

    @Override
    public int countByUserId(Long userId) {
        return sqlSession.selectOne(NAMESPACE + "countByUserId", userId);
    }

    @Override
    public Order findById(Long id) {
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        return sqlSession.selectOne(NAMESPACE + "findByOrderNo", orderNo);
    }

    @Override
    public List<OrderItem> findItemsByOrderId(Long orderId) {
        return sqlSession.selectList(NAMESPACE + "findItemsByOrderId", orderId);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("status", status);
        return sqlSession.update(NAMESPACE + "updateStatus", params);
    }

    @Override
    public int deleteById(Long id) {
        return sqlSession.delete(NAMESPACE + "deleteById", id);
    }
}
