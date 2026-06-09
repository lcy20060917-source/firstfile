package com.ecommerce.controller;

import com.ecommerce.common.Constants;
import com.ecommerce.common.Result;
import com.ecommerce.dto.CartRequest;
import com.ecommerce.dto.CartVO;
import com.ecommerce.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Result<CartVO> view(HttpServletRequest req) {
        return Result.success(cartService.viewCart(getUserId(req)));
    }

    @PostMapping("/add")
    public Result<Void> add(@Valid @RequestBody CartRequest r, HttpServletRequest req) {
        cartService.addToCart(getUserId(req), r.getProductId(),
                r.getQuantity() != null ? r.getQuantity() : 1);
        return Result.success(null);
    }

    @PutMapping("/update")
    public Result<Void> update(@Valid @RequestBody CartRequest r, HttpServletRequest req) {
        cartService.updateQuantity(getUserId(req), r.getProductId(), r.getQuantity());
        return Result.success(null);
    }

    @DeleteMapping("/remove/{productId}")
    public Result<Void> remove(@PathVariable Long productId, HttpServletRequest req) {
        cartService.removeFromCart(getUserId(req), productId);
        return Result.success(null);
    }

    @DeleteMapping("/clear")
    public Result<Void> clear(HttpServletRequest req) {
        cartService.clearCart(getUserId(req));
        return Result.success(null);
    }

    private Long getUserId(HttpServletRequest req) {
        Long uid = (Long) req.getAttribute(Constants.CURRENT_USER_ATTR);
        if (uid == null) throw new RuntimeException("未登录");
        return uid;
    }
}
