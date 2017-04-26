/* Created Apr 26, 2017
 *
 * @(#)NotificationService.java
 *
 * (C)2017 - Emmanuel Akolly
 */
package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.model.Developer;

/**
 * @author efoeakolly
 *
 */
@Service
public class NotificationService {

	private JavaMailSender javaMailSender;

	/**
	 * 
	 * @param javaMailSender
	 */
	@Autowired
	public NotificationService(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}

	/**
	 * 
	 * @param dev
	 * @throws MailException
	 * @throws InterruptedException
	 */
	@Async
	public void sendNotificaitoin(Developer dev) throws MailException, InterruptedException {

		System.out.println("Sleeping now...");
		Thread.sleep(10000);

		System.out.println("Sending email...");

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(dev.getEmailAddress());
		mail.setFrom("akolly@gmail.com");
		mail.setSubject("Spring Boot is awesome!");
		mail.setText("Why aren't you using Spring Boot?");
		
		
		
		javaMailSender.send(mail);

		System.out.println("Email Sent!");
	}

}
