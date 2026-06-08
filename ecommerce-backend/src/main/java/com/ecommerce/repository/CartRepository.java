package com.ecommerce.repository;

import com.ecommerce.domain.CartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartRepository {

        List<CartItem> findByUserId(Long userId);

        CartItem findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

        int insert(CartItem cartItem);

        int updateQuantity(CartItem cartItem);

        int deleteById(Long id);

        int deleteByUserId(Long userId);

        int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
