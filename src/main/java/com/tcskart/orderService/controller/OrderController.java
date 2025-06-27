package com.tcskart.orderService.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcskart.orderService.bean.Order;
import com.tcskart.orderService.service.OrderService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/status/{id}")
	public ResponseEntity<Map<String, Object>> trackOrderStatus(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		 Order order = orderService.trackOrderStatus(id);
		  
		 if(order == null) {
			 response.put("success", true);
			 response.put("message", "No Order Found");
		 }
		
		 else {
			 response.put("success", true);
			 response.put("message", order);
		 }
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/orderhistory/{userid}")
	public ResponseEntity<Map<String, Object>> viewOrderhistory(@PathVariable Long userid){
		
		Map<String, Object> response = new HashMap<>();
		
		List<Order> orderHistory = orderService.viewOrderhistory(userid);
		
		if(orderHistory == null) {
			 response.put("success", true);
			 response.put("message", "No Order History Found");
		 }
		
		 else {
			 response.put("success", true);
			 response.put("message", orderHistory);
		 }
		
		return ResponseEntity.ok(response);
		
	}
	
	
	
	
	

}
