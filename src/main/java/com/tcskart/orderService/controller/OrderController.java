package com.tcskart.orderService.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcskart.orderService.bean.Cart;
import com.tcskart.orderService.bean.Order;
import com.tcskart.orderService.service.OrderService;

@CrossOrigin(origins = "*")  
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

   
    @GetMapping("/status/{orderid}")
    public ResponseEntity<Map<String, Object>> trackOrderStatus(@PathVariable Long orderid) {
        
    	Order order = orderService.trackOrderStatus(orderid);
        
        Map<String, Object> response = new HashMap<>();
		  
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

    // View order history
    @GetMapping("/orderhistories/{userid}")
    public ResponseEntity<Map<String, Object>> viewOrderhistory(@PathVariable Long userid) {
        List<Order> orderHistory = orderService.viewOrderhistory(userid);
        Collections.reverse(orderHistory);
        
        Map<String, Object> response = new HashMap<>();
        
        if(orderHistory == null || orderHistory.isEmpty()) {
			 response.put("success", true);
			 response.put("message", "No Order History Found");
		 }
		
		 else {
			 response.put("success", true);
			 response.put("message", orderHistory);
		 }
		
		return ResponseEntity.ok(response);
    }

    // Place an order
    @PostMapping("/orders/{userId}")
    public ResponseEntity<Map<String, Object>> placeOrder(@PathVariable Long userId,@PathVariable String orderAddress) {
        Order SavedOrder = orderService.placeOrder(userId,orderAddress);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Order placed successfully!");
        response.put("data", SavedOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/orderhistories")
    public ResponseEntity<Map<String, Object>> allOrderHistory() {
    	
        List<Order> orderHistory = orderService.allOrderHistory();
        
        Collections.reverse(orderHistory);
        
        Map<String, Object> response = new HashMap<>();
        
        if(orderHistory == null || orderHistory.isEmpty()) {
			 response.put("success", true);
			 response.put("message", "No Order History Found");
		 }
		
		 else {
			 Collections.reverse(orderHistory);
			 response.put("success", true);
			 response.put("message", orderHistory);
		 }
		
		return ResponseEntity.ok(response);
    }
    
    
}
