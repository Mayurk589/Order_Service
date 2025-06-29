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
		message.setText("The Order details of you "+order.toString() );
		mailSender.send(message);
	}
}
