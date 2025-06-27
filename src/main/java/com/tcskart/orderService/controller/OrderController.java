package com.tcskart.orderService.controller;

import java.util.ArrayList;
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
import com.tcskart.orderService.bean.CartItem;
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
		
		Map<String, Object> response = new HashMap<>();
		
		 Order order = orderService.trackOrderStatus(orderid);
		  
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
		
		if(orderHistory.isEmpty()) {
			 response.put("success", true);
			 response.put("message", "No Order History Found");
		 }
		
		 else {
			 response.put("success", true);
			 response.put("message", orderHistory);
		 }
		
		return ResponseEntity.ok(response);
		
	}
	
    
	
//  ################################################
    
    @PostMapping("/place-order/{userId}")
    public ResponseEntity<Map<String, Object>> placeOrder(@PathVariable Long userId) {
        
        Map<String, Object> response = new HashMap<>();
            
            Cart cart = orderService.placeOrder(userId);

            if (cart == null || cart.getCartItems().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Cart is empty. Cannot place order!");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
           
            response.put("status", "success");
            response.put("message", "Order placed successfully!");
            response.put("data", cart);  

            return new ResponseEntity<>(response, HttpStatus.OK);
         
    }
	
	

}
