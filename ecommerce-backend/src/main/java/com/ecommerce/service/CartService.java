package com.ecommerce.service;

import com.ecommerce.domain.dto.CartVO;

public interface CartService {

        CartVO viewCart(Long userId);

        void addToCart(Long userId, Long productId, Integer quantity);

        void updateQuantity(Long userId, Long productId, Integer quantity);

        void removeFromCart(Long userId, Long productId);

        void clearCart(Long userId);
}
