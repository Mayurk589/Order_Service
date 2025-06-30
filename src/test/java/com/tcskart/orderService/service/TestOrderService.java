package com.tcskart.orderService.service;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tcskart.orderService.bean.Order;
import com.tcskart.orderService.repository.OrderItemRepository;
import com.tcskart.orderService.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrderService {
	
	@Mock
	private OrderRepository orderRepo;

	@Mock
	private OrderItemRepository orderItemRepo;

	@Autowired
	OrderService orderService;
	
	private Order order;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		orderService = new OrderService();
		orderService.orderRepo = orderRepo;
		
        order = new Order();
		order.setOrderId(1L);
		order.setUserId(1L);
        order.setOrderDate(LocalDateTime.now()); 
        order.setOrderStatus("Placed");

	}
	
//    ##########  Test cases for TrackOrderStatus method	
	
	 @Test
	    public void testTrackOrderStatus_Placed() {
	       
	        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

	     
	        Order trackedOrder = orderService.trackOrderStatus(1L);

	        
	        assertNotNull(trackedOrder);
	        assertEquals("Placed", trackedOrder.getOrderStatus());  
	    }
	

	    @Test
	    public void testTrackOrderStatus_AlreadyDelivered() {
	        
	        order.setOrderStatus("Delivered");  
	        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

	       
	        Order trackedOrder = orderService.trackOrderStatus(1L);

	       
	        assertNotNull(trackedOrder);
	        assertEquals("Delivered", trackedOrder.getOrderStatus());  
	    }


	    @Test
	    public void testTrackOrderStatus_OnTheWay() {
	        
	        order.setOrderDate(LocalDateTime.now().minusDays(2));  
	        
	        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

	        
	        Order trackedOrder = orderService.trackOrderStatus(1L);

	        
	        assertNotNull(trackedOrder);
	        assertEquals("On The Way", trackedOrder.getOrderStatus());  
	    }
	    
	    @Test
	    public void testTrackOrderStatus_DeliveredAfterThreeDays() {
	        
	        order.setOrderDate(LocalDateTime.now().minusDays(4));  
	        
	        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

	        
	        Order trackedOrder = orderService.trackOrderStatus(1L);

	        
	        assertNotNull(trackedOrder);
	        assertEquals("Delivered", trackedOrder.getOrderStatus());  
	    }

	    @Test
	    public void testTrackOrderStatus_NotFound() {
	       
	        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

	        
	        Order trackedOrder = orderService.trackOrderStatus(1L);

	        
	        assertNull(trackedOrder);  
	    }
	    
	    
//	    ##########  Test cases for ViewOrderHistory method
	    
	    @Test
	    public void testViewOrderHistory() {
	    	
	    	List<Order> orderHistory = new ArrayList<>();
	    	orderHistory.add(order);
	       
	        when(orderRepo.findByUserId(1L)).thenReturn(orderHistory);

	     
	        List<Order> trackedOrderHistory = orderService.viewOrderhistory(1L);

	        
	        assertNotNull(trackedOrderHistory);
	        assertEquals(trackedOrderHistory, trackedOrderHistory);  
	    }
	    
	    
	    
	    
	

	

}
