package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.OrderVO;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.CartMapper;
import com.ecommerce.mapper.OrderItemMapper;
import com.ecommerce.mapper.OrderMapper;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                            CartMapper cartMapper, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public OrderVO createOrder(Long userId, OrderRequest request) {
        List<CartItem> cartItems = cartMapper.selectList(
                new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, userId));
        if (cartItems.isEmpty()) throw new BusinessException(400, "购物车为空");

        String orderNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 8);

        BigDecimal total = BigDecimal.ZERO;
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setStatus(0);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setTotalAmount(BigDecimal.ZERO);
        orderMapper.insert(order);

        for (CartItem ci : cartItems) {
            Product p = productMapper.selectById(ci.getProductId());
            if (p == null || p.getStock() < ci.getQuantity()) {
                throw new BusinessException(400, "商品 [" + (p != null ? p.getName() : ci.getProductId()) + "] 库存不足");
            }
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setProductId(p.getId());
            oi.setProductName(p.getName());
            oi.setProductPrice(p.getPrice());
            oi.setQuantity(ci.getQuantity());
            BigDecimal sub = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            oi.setTotalPrice(sub);
            total = total.add(sub);
            orderItemMapper.insert(oi);

            p.setStock(p.getStock() - ci.getQuantity());
            productMapper.updateById(p);
        }

        order.setTotalAmount(total);
        orderMapper.updateById(order);
        cartMapper.delete(new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, userId));
        log.info("订单创建: orderNo={}, amount={}", orderNo, total);
        return toVO(order, orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId())));
    }

    @Override
    public IPage<OrderVO> listOrders(Long userId, int page, int size) {
        IPage<Order> p = orderMapper.selectPage(new Page<>(page + 1, size),
                new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId)
                        .orderByDesc(Order::getCreateTime));
        return p.convert(o -> toVO(o, null));
    }

    @Override
    public OrderVO getOrderDetail(Long orderId) {
        Order o = orderMapper.selectById(orderId);
        if (o == null) throw new BusinessException(404, "订单不存在");
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        return toVO(o, items);
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order o = orderMapper.selectById(orderId);
        if (o == null) throw new BusinessException(404, "订单不存在");
        if (!o.getUserId().equals(userId)) throw new BusinessException(403, "无权操作");
        if (o.getStatus() != 0) throw new BusinessException(400, "只能取消待支付订单");
        o.setStatus(2);
        orderMapper.updateById(o);
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        for (OrderItem oi : items) {
            Product p = productMapper.selectById(oi.getProductId());
            if (p != null) {
                p.setStock(p.getStock() + oi.getQuantity());
                productMapper.updateById(p);
            }
        }
        log.info("订单取消: orderId={}", orderId);
    }

    private OrderVO toVO(Order o, List<OrderItem> items) {
        OrderVO vo = new OrderVO();
        vo.setId(o.getId());
        vo.setOrderNo(o.getOrderNo());
        vo.setUserId(o.getUserId());
        vo.setTotalAmount(o.getTotalAmount());
        vo.setStatus(o.getStatus());
        vo.setReceiverName(o.getReceiverName());
        vo.setReceiverPhone(o.getReceiverPhone());
        vo.setReceiverAddress(o.getReceiverAddress());
        vo.setCreateTime(o.getCreateTime());
        vo.setUpdateTime(o.getUpdateTime());

        if (items != null) {
            vo.setItems(items.stream().map(oi -> {
                OrderVO.OrderItemVO iv = new OrderVO.OrderItemVO();
                iv.setId(oi.getId());
                iv.setProductId(oi.getProductId());
                iv.setProductName(oi.getProductName());
                iv.setProductPrice(oi.getProductPrice());
                iv.setQuantity(oi.getQuantity());
                iv.setTotalPrice(oi.getTotalPrice());
                return iv;
            }).collect(Collectors.toList()));
        }
        return vo;
    }
}
