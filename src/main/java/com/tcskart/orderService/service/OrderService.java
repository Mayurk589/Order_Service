package com.tcskart.orderService.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcskart.orderService.bean.Order;
import com.tcskart.orderService.repository.OrderRepository;


@Service
public class OrderService { 
	
	@Autowired
	OrderRepository orderRepo;

	public Order trackOrderStatus(Long id) {
		
		Optional<Order> optionalOrder = orderRepo.findById(id);
		
		if(optionalOrder.isPresent()) {
			
			Order order = optionalOrder.get();
			
			if(order.getOrderStatus().equals("Delivered")) {
				return order;
			}
			
			LocalDate orderDate = order.getOrderDate().toLocalDate();
			LocalDate currentDate = LocalDate.now();
			
			long daysDifference = ChronoUnit.DAYS.between(orderDate, currentDate);
			
			if(daysDifference < 1) {
				order.setOrderStatus("Placed");
				
				orderRepo.save(order);
			}
			else if(daysDifference < 3) {
				order.setOrderStatus("On The Way");
				
				orderRepo.save(order);
			}
			else {
				order.setOrderStatus("Delivered");
				orderRepo.save(order);
			}
			
			return order;
		}
		
		return null;
	}

	public List<Order> viewOrderhistory(Long userId) {
		
		List<Order> orderHistory = orderRepo.findByUserId(userId);
		
		return orderHistory;
	}

}
