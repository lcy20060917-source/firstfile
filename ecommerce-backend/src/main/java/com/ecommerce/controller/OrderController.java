package com.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecommerce.common.Constants;
import com.ecommerce.common.Result;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.OrderVO;
import com.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    public Result<OrderVO> create(@Valid @RequestBody OrderRequest r, HttpServletRequest req) {
        Long uid = getUserId(req);
        log.info("下单: userId={}", uid);
        return Result.success(orderService.createOrder(uid, r));
    }

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest req) {
        Long uid = getUserId(req);
        IPage<OrderVO> p = orderService.listOrders(uid, page, size);
        return Result.success(Map.of("content", p.getRecords(), "total", p.getTotal(),
                "pages", p.getPages(), "current", p.getCurrent()));
    }

    @GetMapping("/{id}")
    public Result<OrderVO> detail(@PathVariable Long id, HttpServletRequest req) {
        OrderVO vo = orderService.getOrderDetail(id);
        if (!vo.getUserId().equals(getUserId(req))) return Result.error(403, "无权查看");
        return Result.success(vo);
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id, HttpServletRequest req) {
        orderService.cancelOrder(getUserId(req), id);
        return Result.success(null);
    }

    private Long getUserId(HttpServletRequest req) {
        Long uid = (Long) req.getAttribute(Constants.CURRENT_USER_ATTR);
        if (uid == null) throw new RuntimeException("未登录");
        return uid;
    }
}
