package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecommerce.dto.CartVO;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.mapper.CartMapper;
import com.ecommerce.mapper.ProductMapper;
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
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    public CartServiceImpl(CartMapper cartMapper, ProductMapper productMapper) {
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
    }

    @Override
    public CartVO viewCart(Long userId) {
        List<CartItem> items = cartMapper.selectList(
                new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, userId));
        CartVO vo = new CartVO();
        List<CartVO.CartItemVO> vos = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;

        for (CartItem ci : items) {
            Product p = productMapper.selectById(ci.getProductId());
            CartVO.CartItemVO iv = new CartVO.CartItemVO();
            iv.setId(ci.getId());
            iv.setProductId(ci.getProductId());
            iv.setQuantity(ci.getQuantity());
            if (p != null) {
                iv.setProductName(p.getName());
                iv.setProductPrice(p.getPrice());
                iv.setProductImageUrl(p.getImageUrl());
                BigDecimal sub = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
                iv.setSubtotal(sub);
                total = total.add(sub);
            }
            count += ci.getQuantity();
            vos.add(iv);
        }
        vo.setItems(vos);
        vo.setTotalAmount(total);
        vo.setTotalCount(count);
        return vo;
    }

    @Override
    @Transactional
    public void addToCart(Long userId, Long productId, Integer quantity) {
        Product p = productMapper.selectById(productId);
        if (p == null || p.getStock() <= 0) {
            throw new BusinessException(400, "商品不存在或库存不足");
        }
        CartItem exist = cartMapper.selectOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId).eq(CartItem::getProductId, productId));
        if (exist != null) {
            exist.setQuantity(exist.getQuantity() + quantity);
            cartMapper.updateById(exist);
        } else {
            CartItem ci = new CartItem();
            ci.setUserId(userId);
            ci.setProductId(productId);
            ci.setQuantity(quantity);
            cartMapper.insert(ci);
        }
        log.info("加入购物车: userId={}, productId={}, qty={}", userId, productId, quantity);
    }

    @Override
    @Transactional
    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) { removeFromCart(userId, productId); return; }
        CartItem ci = cartMapper.selectOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId).eq(CartItem::getProductId, productId));
        if (ci == null) throw new BusinessException(404, "购物车中无此商品");
        ci.setQuantity(quantity);
        cartMapper.updateById(ci);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        cartMapper.delete(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId).eq(CartItem::getProductId, productId));
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartMapper.delete(new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, userId));
    }
}
