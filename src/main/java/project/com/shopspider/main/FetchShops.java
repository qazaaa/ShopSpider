package com.shopspider.main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class FetchShops
{
	static
	{
		System.setProperty("workdir", FetchShops.class.getResource("/")
		        .getFile());
		PropertyConfigurator.configure(FetchShops.class.getResource("/")
		        .getFile() + "log4j.properties");
	}

	static Logger logger = Logger.getLogger(FetchShops.class.getName());

	public static void main(String[] args)
	{
		System.out.println(logger);
		boolean bContinue = false;
		if (args.length > 0 && args[0].equals("1"))
		{
			bContinue = true;
		}
		ProcessControl process = ProcessControlImpl.getSingleton();
		process.beginFetchShops(bContinue);

	}
}
