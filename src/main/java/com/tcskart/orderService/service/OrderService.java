package com.tcskart.orderService.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcskart.orderService.bean.Cart;
import com.tcskart.orderService.bean.CartItem;
import com.tcskart.orderService.bean.Order;
import com.tcskart.orderService.bean.OrderItem;
import com.tcskart.orderService.repository.OrderRepository;


@Service
public class OrderService { 
	
	 @Autowired
	 private RestTemplate restTemplate; 
	
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
	
	
	   
	   public Cart placeOrder(Long userId) {
	        
	        Cart cart = getCartByUserId(userId);

	        
	        if (cart == null || cart.getCartItems().isEmpty()) {
	            return null; 
	        }
	        
	        
	        Order order = new Order();
	        order.setUserId(userId);
	        order.setTotalAmount(cart.getTotalPrice()); 
	        order.setOrderStatus("Order Placed");

	        
	        List<OrderItem> orderItems = new ArrayList<>();
	        for (CartItem cartItem : cart.getCartItems()) {
	            OrderItem orderItem = new OrderItem();
	            orderItem.setProductId(cartItem.getProductId());
	            orderItem.setQuantity(cartItem.getQuantity());
	            orderItem.setPriceAtOrder(cartItem.getPrice());
	            orderItem.setOrder(order); 
	            orderItems.add(orderItem);
	        }

	        order.setItems(orderItems);

	        
	        orderRepo.save(order);
	       
	        // call the cart service for delete the cart table based on the user id
	        deleteCartByUserId(userId);
	        
	        return cart; 
	    }

	    
	   private void deleteCartByUserId(Long userId) {
		   
		    String cartServiceUrl = "http://localhost:8083/cart/delete";
	        
	        String deleteCartUrl = cartServiceUrl + "/" + userId;

	        restTemplate.exchange(deleteCartUrl, HttpMethod.DELETE, null, Void.class);
	    }

		private Cart getCartByUserId(Long userId) {
	        
	        String cartServiceUrl = "http://localhost:8083/cart/view/" + userId;

	        
	        ResponseEntity<Cart> response = restTemplate.exchange(cartServiceUrl, HttpMethod.GET, null, Cart.class);

	        
	        if (response.getStatusCode().is2xxSuccessful()) {
	            return response.getBody();  
	        } else {
	            return null;  
	        }
	    }
		

		public List<Order> allOrderHistory() {
			
			List<Order> orderHistory = orderRepo.findAll();
			
			return orderHistory;
			
		}

}
