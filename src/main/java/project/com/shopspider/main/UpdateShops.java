package com.shopspider.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shopspider.thread.UpdateShopDetailThread;

public class UpdateShops
{
	public static void main(String[] args)
	{
		PropertyConfigurator.configure(FetchShops.class.getResource("/")
		        .getFile() + "log4j.properties");
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        "beans.xml");

		ExecutorService executor = Executors.newCachedThreadPool();

		UpdateShopDetailThread updateShopDetailThread = context
		        .getBean(UpdateShopDetailThread.class);
		executor.submit(updateShopDetailThread);

	}
}
