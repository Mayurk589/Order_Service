package com.tcskart.orderService.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;



public @Data class Cart {

    private Long cartId;
    private Long userId;
    private List<CartItem> cartItems = new ArrayList<>();
    private Double totalPrice;

    public Cart(Long userId, Double totalPrice) {
        this.userId = userId;
        this.cartItems = null;
        this.totalPrice = totalPrice;
    }

	public Cart() {
		// TODO Auto-generated constructor stub
	}
}
