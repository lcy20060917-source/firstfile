package com.ecommerce.controller;

import com.ecommerce.common.Constants;
import com.ecommerce.common.Result;
import com.ecommerce.domain.dto.CartRequest;
import com.ecommerce.domain.dto.CartVO;
import com.ecommerce.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Result<CartVO> viewCart(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.debug("查看购物车: userId={}", userId);
        CartVO cart = cartService.viewCart(userId);
        return Result.success(cart);
    }

    @PostMapping("/add")
    public Result<Void> addToCart(@Valid @RequestBody CartRequest cartRequest, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("加入购物车: userId={}, productId={}, quantity={}",
                userId, cartRequest.getProductId(), cartRequest.getQuantity());
        cartService.addToCart(userId, cartRequest.getProductId(),
                cartRequest.getQuantity() != null ? cartRequest.getQuantity() : 1);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateQuantity(@Valid @RequestBody CartRequest cartRequest, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("更新购物车: userId={}, productId={}, quantity={}",
                userId, cartRequest.getProductId(), cartRequest.getQuantity());
        cartService.updateQuantity(userId, cartRequest.getProductId(), cartRequest.getQuantity());
        return Result.success();
    }

    @DeleteMapping("/remove/{productId}")
    public Result<Void> removeFromCart(@PathVariable Long productId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("移除购物车商品: userId={}, productId={}", userId, productId);
        cartService.removeFromCart(userId, productId);
        return Result.success();
    }

    @DeleteMapping("/clear")
    public Result<Void> clearCart(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("清空购物车: userId={}", userId);
        cartService.clearCart(userId);
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
