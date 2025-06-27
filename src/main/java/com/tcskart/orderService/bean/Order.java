package com.tcskart.orderService.bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="orders")
public @Data  class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	private long userId;
	
	private double totalAmount;
	
	private String orderStatus;
	
	private LocalDateTime orderDate = LocalDateTime.now();
	
	
	@OneToMany(mappedBy ="order", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<OrderItem> items = new ArrayList<>();
	
}
