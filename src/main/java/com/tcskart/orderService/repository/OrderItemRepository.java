package com.tcskart.orderService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcskart.orderService.bean.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
