package com.ecommerce.service.impl;

import com.ecommerce.common.Constants;
import com.ecommerce.domain.CartItem;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.OrderItem;
import com.ecommerce.domain.Product;
import com.ecommerce.domain.dto.OrderRequest;
import com.ecommerce.domain.dto.OrderVO;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrder(Long userId, OrderRequest request) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new BusinessException(400, "购物车为空，无法下单");
        }
        String orderNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 8);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId());
            if (product == null || product.getStatus() != Constants.ProductStatus.ON_SALE) {
                throw new BusinessException(400, "商品 [" + cartItem.getProductName() + "] 已下架");
            }
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BusinessException(400,
                        "商品 [" + product.getName() + "] 库存不足，当前库存: " + product.getStock());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItems.add(orderItem);

            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(Constants.OrderStatus.PENDING);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());

        orderRepository.insertOrder(order);
        log.info("创建订单: orderNo={}, userId={}, amount={}", orderNo, userId, totalAmount);
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }
        orderRepository.insertOrderItems(orderItems);
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId());
            int newStock = product.getStock() - item.getQuantity();
            productRepository.updateStock(product.getId(), newStock);
            log.debug("扣减库存: productId={}, {} -> {}", product.getId(), product.getStock(), newStock);
        }
        cartRepository.deleteByUserId(userId);
        return toOrderVO(order, orderItems);
    }

    @Override
    public List<OrderVO> listOrders(Long userId, int page, int size) {
        int offset = page * size;
        int limit = Math.min(size, 50);
        List<Order> orders = orderRepository.findByUserId(userId, offset, limit);
        List<OrderVO> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(toOrderVO(order, null));
        }
        return result;
    }

    @Override
    public int countOrders(Long userId) {
        return orderRepository.countByUserId(userId);
    }

    @Override
    public OrderVO getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        List<OrderItem> items = orderRepository.findItemsByOrderId(orderId);
        return toOrderVO(order, items);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (order.getStatus() != Constants.OrderStatus.PENDING) {
            throw new BusinessException(400, "只能取消待支付状态的订单");
        }

        orderRepository.updateStatus(orderId, Constants.OrderStatus.CANCELLED);
        List<OrderItem> items = orderRepository.findItemsByOrderId(orderId);
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId());
            if (product != null) {
                int newStock = product.getStock() + item.getQuantity();
                productRepository.updateStock(product.getId(), newStock);
            }
        }

        log.info("取消订单: orderNo={}", order.getOrderNo());
    }

    private OrderVO toOrderVO(Order order, List<OrderItem> items) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setUserId(order.getUserId());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());
        vo.setReceiverName(order.getReceiverName());
        vo.setReceiverPhone(order.getReceiverPhone());
        vo.setReceiverAddress(order.getReceiverAddress());
        vo.setCreateTime(order.getCreateTime());
        vo.setUpdateTime(order.getUpdateTime());

        if (items != null) {
            List<OrderVO.OrderItemVO> itemVOs = new ArrayList<>();
            for (OrderItem item : items) {
                OrderVO.OrderItemVO itemVO = new OrderVO.OrderItemVO();
                itemVO.setId(item.getId());
                itemVO.setProductId(item.getProductId());
                itemVO.setProductName(item.getProductName());
                itemVO.setProductPrice(item.getProductPrice());
                itemVO.setQuantity(item.getQuantity());
                itemVO.setTotalPrice(item.getTotalPrice());
                itemVOs.add(itemVO);
            }
            vo.setItems(itemVOs);
        }

        return vo;
    }
}
