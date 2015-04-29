package com.shopspider.thread;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shopspider.bean.ShopBean;
import com.shopspider.dao.ShopDao;

@Component("removeDuplicateThread")
public class RemoveDuplicateThread implements Runnable
{
	Logger logger = Logger.getLogger(RemoveDuplicateThread.class);
	private static final int TIME_INTERVAL_IN_MILLISECOND = 60 * 60 * 1000;

	@Resource
	ShopDao shopDao;

	@Override
	public void run()
	{
		while (!Thread.interrupted())
		{
			List<ShopBean> shops = shopDao.getAllShopsOrderByUrl();
			logger.info("Get " + shops.size() + " shops to operate");

			if (shops != null && shops.size() > 1)
			{
				List<ShopBean> shopsToDelete = new ArrayList<ShopBean>();

				String lastUrl = shops.get(0).getShopUrl().intern();

				for (int i = 1; i < shops.size(); i++)
				{
					String shopUrl = shops.get(i).getShopUrl();
					if (shopUrl == null || shopUrl.equals(""))
					{
						logger.warn(shops.get(i).getShopName()
						        + "'s shop url is null or empty");
						continue;
					}
					if (shopUrl.equals(lastUrl))
					{
						shopsToDelete.add(shops.get(i));
						logger.info("Need to delete id:"
						        + shops.get(i).getShopId() + " url:"
						        + shops.get(i).getShopUrl());
					} else
					{
						lastUrl = shopUrl.intern();
					}

				}

				if (shopsToDelete.size() > 0)
				{
					logger.info("There are " + shopsToDelete.size()
					        + " shops to delete!");
					shopDao.deleteAll(shopsToDelete);

				} else
				{
					logger.info("There are no shops to delete!");
				}

				try
				{
					Thread.sleep(TIME_INTERVAL_IN_MILLISECOND);
				} catch (InterruptedException e)
				{
					logger.info("The thread is interrupted,and thread will exit");
					Thread.currentThread().interrupt();
				}

			}

		}

	}
}
