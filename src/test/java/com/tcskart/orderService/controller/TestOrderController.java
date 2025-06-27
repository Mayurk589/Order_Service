package com.tcskart.orderService.controller;


import com.tcskart.orderService.bean.Order;
import com.tcskart.orderService.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class TestOrderController {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
        order.setOrderId(1L);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("Placed");
    }

    @Test
    public void testTrackOrderStatus_Found() {
       
        when(orderService.trackOrderStatus(1L)).thenReturn(order);

        
        ResponseEntity<Map<String, Object>> response = orderController.trackOrderStatus(1L);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((boolean) response.getBody().get("success"));

    }

    @Test
    public void testTrackOrderStatus_NotFound() {
        
        when(orderService.trackOrderStatus(1L)).thenReturn(null);

       
        ResponseEntity<Map<String, Object>> response = orderController.trackOrderStatus(1L);

       
        assertEquals(HttpStatus.OK, response.getStatusCode());
       
    }
}

