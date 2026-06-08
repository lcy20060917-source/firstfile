package com.ecommerce.service.impl;

import com.ecommerce.domain.CartItem;
import com.ecommerce.domain.Product;
import com.ecommerce.domain.dto.CartVO;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartVO viewCart(Long userId) {
        List<CartItem> items = cartRepository.findByUserId(userId);

        CartVO cartVO = new CartVO();
        List<CartVO.CartItemVO> itemVOs = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalCount = 0;

        for (CartItem item : items) {
            CartVO.CartItemVO vo = new CartVO.CartItemVO();
            vo.setId(item.getId());
            vo.setProductId(item.getProductId());
            vo.setProductName(item.getProductName());
            vo.setProductPrice(item.getProductPrice());
            vo.setProductImageUrl(item.getProductImageUrl());
            vo.setQuantity(item.getQuantity());

            BigDecimal subtotal = item.getProductPrice() != null
                    ? item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                    : BigDecimal.ZERO;
            vo.setSubtotal(subtotal);

            totalAmount = totalAmount.add(subtotal);
            totalCount += item.getQuantity();
            itemVOs.add(vo);
        }

        cartVO.setItems(itemVOs);
        cartVO.setTotalAmount(totalAmount);
        cartVO.setTotalCount(totalCount);

        return cartVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToCart(Long userId, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        if (product.getStock() <= 0) {
            throw new BusinessException(400, "商品库存不足");
        }
        CartItem existing = cartRepository.findByUserIdAndProductId(userId, productId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartRepository.updateQuantity(existing);
            log.info("更新购物车: userId={}, productId={}, quantity={}", userId, productId, existing.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartRepository.insert(cartItem);
            log.info("加入购物车: userId={}, productId={}, quantity={}", userId, productId, quantity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            removeFromCart(userId, productId);
            return;
        }

        CartItem cartItem = cartRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem == null) {
            throw new BusinessException(404, "购物车中无此商品");
        }

        cartItem.setQuantity(quantity);
        cartRepository.updateQuantity(cartItem);
        log.info("更新购物车数量: userId={}, productId={}, quantity={}", userId, productId, quantity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFromCart(Long userId, Long productId) {
        cartRepository.deleteByUserIdAndProductId(userId, productId);
        log.info("移除购物车商品: userId={}, productId={}", userId, productId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
        log.info("清空购物车: userId={}", userId);
    }
}
