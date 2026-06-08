package com.ecommerce.controller;

import com.ecommerce.common.Constants;
import com.ecommerce.common.Result;
import com.ecommerce.domain.dto.OrderRequest;
import com.ecommerce.domain.dto.OrderVO;
import com.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Result<OrderVO> createOrder(@Valid @RequestBody OrderRequest orderRequest,
                                        HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("创建订单: userId={}, receiverName={}", userId, orderRequest.getReceiverName());
        OrderVO order = orderService.createOrder(userId, orderRequest);
        return Result.success(order);
    }

    @GetMapping
    public Result<Map<String, Object>> listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.debug("查询订单列表: userId={}, page={}, size={}", userId, page, size);

        List<OrderVO> orders = orderService.listOrders(userId, page, size);
        int total = orderService.countOrders(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("content", orders);
        data.put("totalElements", total);
        data.put("totalPages", (int) Math.ceil((double) total / size));
        data.put("size", size);
        data.put("number", page);

        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.debug("查询订单详情: userId={}, orderId={}", userId, id);
        OrderVO order = orderService.getOrderDetail(id);
        if (!order.getUserId().equals(userId)) {
            return Result.error(403, "无权查看此订单");
        }

        return Result.success(order);
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("取消订单: userId={}, orderId={}", userId, id);
        orderService.cancelOrder(userId, id);
        return Result.success();
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ATTR);
        if (userId == null) {
            throw new RuntimeException("未登录或 Token 已过期");
        }
        return userId;
    }
}
