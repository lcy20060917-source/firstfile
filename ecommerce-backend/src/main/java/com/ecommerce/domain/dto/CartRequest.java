package com.ecommerce.domain.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartRequest {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @Min(value = 1, message = "数量至少为1")
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartRequest{productId=" + productId + ", quantity=" + quantity + "}";
    }
}
