
package com.tcskart.orderService.bean;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="cart_items")
public @Data class CartItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long cartId;
	private long userId;
	private long prioductId;
	private int quantity;
	private LocalDateTime addedAt=LocalDateTime.now();
}
