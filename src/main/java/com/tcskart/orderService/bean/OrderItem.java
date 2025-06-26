package com.tcskart.orderService.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long orderItemId;
	
	private Long productId;
	
	private Integer quantity;
	
	private double priceAtOrder;
	
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;
	
}
