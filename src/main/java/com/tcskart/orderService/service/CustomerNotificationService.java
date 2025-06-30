package com.tcskart.orderService.service;

import lombok.AllArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tcskart.orderService.bean.Order;

@Service
@AllArgsConstructor
public class CustomerNotificationService {

	private final JavaMailSender mailSender;

	public void  notifySuccussfullOrder(Order order) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("shriharisekar@gmail.com");
		message.setSubject("Order Placed Successfully: " + order.getOrderId());
		message.setText("Your Order details as follows: \n"+"Your user Id: "+order.getUserId()+
				"\n Order Id: "+order.getOrderId()+
				"\n Order address: "+order.getOrderAdress()+
				"\n Total amount on order: "+order.getTotalAmount()+
				"\n Order status: "+order.getOrderStatus());
		mailSender.send(message);
	}
}
