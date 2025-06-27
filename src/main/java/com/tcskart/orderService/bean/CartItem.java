package com.tcskart.orderService.bean;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    private Long cartItemId;
    private Long cartId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double price;
    private LocalDateTime addedAt;

    public CartItem(Long cartId, Long userId, Long productId, Integer quantity, Double price) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.addedAt = LocalDateTime.now();
    }
}
