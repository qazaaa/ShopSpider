package com.shopspider.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shopspider.thread.RemoveDuplicateThread;

public class RemoveDuplicate
{
	public static void main(String[] args)
	{
		PropertyConfigurator.configure(FetchShops.class.getResource("/")
		        .getFile() + "log4j.properties");
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        "beans.xml");

		ExecutorService executor = Executors.newCachedThreadPool();

		RemoveDuplicateThread removeDuplicateThread = context
		        .getBean(RemoveDuplicateThread.class);
		executor.submit(removeDuplicateThread);

	}
}
