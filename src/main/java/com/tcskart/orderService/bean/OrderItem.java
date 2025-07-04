package com.tcskart.orderService.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="order_items")
public @Data class OrderItem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long orderItemId;
	
	private Long productId;
	
	private String imgUrl;
	
	private Integer quantity; 
	
	private double priceAtOrder; 
	
	@ManyToOne
	@JoinColumn(name="order_id")
	@JsonBackReference
	private Order order;
	
}
