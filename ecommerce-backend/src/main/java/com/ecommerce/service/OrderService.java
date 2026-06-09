package com.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.OrderVO;

public interface OrderService {
    OrderVO createOrder(Long userId, OrderRequest request);
    IPage<OrderVO> listOrders(Long userId, int page, int size);
    OrderVO getOrderDetail(Long orderId);
    void cancelOrder(Long userId, Long orderId);
}
