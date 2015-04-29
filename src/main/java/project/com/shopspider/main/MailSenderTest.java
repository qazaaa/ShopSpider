package com.shopspider.main;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailSenderTest
{
	public static void main(String[] args)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        "beans.xml");
		JavaMailSender mailSender = (JavaMailSender) context
		        .getBean("mailSender");
		MimeMessage message = mailSender.createMimeMessage();
		try
		{
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("shopspider@163.com");
			helper.setTo("shopspider@163.com");
			helper.setSubject("test");
			helper.setText("yes");
			mailSender.send(message);
		} catch (MessagingException e)
		{
			e.printStackTrace();
		}

	}
}
