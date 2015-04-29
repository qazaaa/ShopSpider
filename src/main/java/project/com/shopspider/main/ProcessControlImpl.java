package com.shopspider.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shopspider.common.Consts;
import com.shopspider.container.KeyWordContainer;
import com.shopspider.container.PageCollectionsContainer;
import com.shopspider.container.ShopsContainer;
import com.shopspider.thread.InitialParseThread;
import com.shopspider.thread.ParseListByKeyWordThread;
import com.shopspider.thread.ParseShopListPageThread;
import com.shopspider.thread.SchedualSaveBDBThread;
import com.shopspider.thread.ShopDBThread;

public class ProcessControlImpl implements ProcessControl
{

	Logger logger = Logger.getLogger(ProcessControlImpl.class);

	ApplicationContext context = null;

	private ProcessControlImpl()
	{
		context = new ClassPathXmlApplicationContext("beans.xml");
	}

	private static class SingletonCapsule
	{
		private static ProcessControl singleton = new ProcessControlImpl();
	}

	public static ProcessControl getSingleton()
	{
		return SingletonCapsule.singleton;

	}

	@Override
	public void beginFetchShops(boolean bContinue)
	{
		logger.info("The process's bContinue is :" + bContinue);

		// 定时保存线程
		SchedualSaveBDBThread schedualSaveBDBThread = context
		        .getBean(SchedualSaveBDBThread.class);

		// containers
		PageCollectionsContainer pageCollectionsContainer = new PageCollectionsContainer();
		ShopsContainer shopsContainer = (ShopsContainer) context
		        .getBean("shopsContainer");
		KeyWordContainer keyWordContainer = new KeyWordContainer();
		schedualSaveBDBThread
		        .setPageCollectionsContainer(pageCollectionsContainer);
		schedualSaveBDBThread.setShopsContainer(shopsContainer);
		schedualSaveBDBThread.setKeyWordContainer(keyWordContainer);

		// 初始解析线程
		InitialParseThread initialPareseThread = (InitialParseThread) context
		        .getBean(InitialParseThread.class);
		schedualSaveBDBThread.setInitialParseThread(initialPareseThread);
		initialPareseThread
		        .SetPageCollectionsContainer(pageCollectionsContainer);

		if (bContinue)
		{
			schedualSaveBDBThread.load();
			initialPareseThread.setBContinue(bContinue);
		}

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(initialPareseThread);

		for (int i = 0; i < 5; i++)
		{
			ParseShopListPageThread p = context
			        .getBean(ParseShopListPageThread.class);
			schedualSaveBDBThread.addParseShopListPageThread(p);
			p.setPageCollectionsContainer(pageCollectionsContainer);
			p.setShopsContainer(shopsContainer);
			p.setKeyWordContainer(keyWordContainer);
			executor.execute(p);
		}

		for (int i = 0; i < 10; i++)
		{
			ParseListByKeyWordThread parseListByKeyWordThread = (ParseListByKeyWordThread) context
			        .getBean(ParseListByKeyWordThread.class);
			schedualSaveBDBThread
			        .addParseListByKeyWordThread(parseListByKeyWordThread);
			parseListByKeyWordThread.setKeyWordContainer(keyWordContainer);
			parseListByKeyWordThread
			        .setPageCollectionsContainer(pageCollectionsContainer);
			executor.execute(parseListByKeyWordThread);
		}

		ShopDBThread shopDBTread = context.getBean(ShopDBThread.class);
		schedualSaveBDBThread.addShopDBThread(shopDBTread);
		shopDBTread.setShopsContainer(shopsContainer);
		executor.execute(shopDBTread);

		// 定时保存运行时数据
		ScheduledThreadPoolExecutor schedual = new ScheduledThreadPoolExecutor(
		        1);
		schedual.scheduleWithFixedDelay(schedualSaveBDBThread,
		        Consts.SCHEDUALE_SAVE_INTERVAL_IN_SECOND,
		        Consts.SCHEDUALE_SAVE_INTERVAL_IN_SECOND, TimeUnit.SECONDS);

	}

}
