package com.ecommerce.service;

import com.ecommerce.domain.dto.OrderRequest;
import com.ecommerce.domain.dto.OrderVO;

import java.util.List;

public interface OrderService {

        OrderVO createOrder(Long userId, OrderRequest request);

        List<OrderVO> listOrders(Long userId, int page, int size);

        int countOrders(Long userId);

        OrderVO getOrderDetail(Long orderId);

        void cancelOrder(Long userId, Long orderId);
}
