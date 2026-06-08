package com.ecommerce.repository;

import com.ecommerce.domain.Order;
import com.ecommerce.domain.OrderItem;

import java.util.List;

public interface OrderRepository {

        int insertOrder(Order order);

        int insertOrderItems(List<OrderItem> items);

        List<Order> findByUserId(Long userId, int offset, int limit);

        int countByUserId(Long userId);

        Order findById(Long id);

        Order findByOrderNo(String orderNo);

        List<OrderItem> findItemsByOrderId(Long orderId);

        int updateStatus(Long id, Integer status);

        int deleteById(Long id);
}
