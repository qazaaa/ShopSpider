package com.shopspider.thread;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shopspider.bean.ShopBean;
import com.shopspider.dao.ShopDao;
import com.shopspider.shopService.ShopService;

@Component("updateShopDetailThread")
public class UpdateShopDetailThread implements Runnable
{
	Logger logger = Logger.getLogger(UpdateShopDetailThread.class);
	@Resource
	ShopDao shopDao;

	@Resource
	ShopService shopService;

	private static final int FROM_INDEX = 0;
	private static final int HANDLE_COUNT_PER_TIME = 100;
	private static final int UPDATE_TIMEINTERVAL_IN_SECOND = 86400 * 20;// 20
	                                                                    // days

	@Override
	public void run()
	{
		int fromIndex = FROM_INDEX;
		while (!Thread.interrupted())
		{
			List<ShopBean> shops = shopDao.queryShops(
			        "where updated_at is null or TIMESTAMPDIFF(SECOND,updated_at, NOW()) < "
			                + UPDATE_TIMEINTERVAL_IN_SECOND + " ORDER BY id",
			        fromIndex, HANDLE_COUNT_PER_TIME);
			if (shops == null || shops.size() == 0)
			{
				logger.info("There is no shops to update ,thread will sleep");
				fromIndex = FROM_INDEX;
				try
				{
					Thread.sleep(10000);
				} catch (InterruptedException e)
				{
					logger.info("The UpdateShopDetailThread is interrupted and thread will exit");
					Thread.currentThread().interrupt();
				}
			}
			logger.info("Get " + shops.size() + " from database to update");
			for (ShopBean shopBean : shops)
			{
				try
				{
					shopService.updateShopDetail(shopBean);
				} catch (Exception e)
				{
					logger.error("Catch exception when update page: "
					        + shopBean.getShopUrl() + "     exception:"
					        + e.getMessage());
				}
				try
				{
					Thread.sleep(10000);
				} catch (InterruptedException e)
				{
					logger.info("The UpdateShopDetailThread is interrupted and will exit");
					Thread.currentThread().interrupt();
				}
			}

			shopDao.updateAll(shops);
			logger.info(shops.size() + " shops is updated!");

			fromIndex += shops.get(shops.size() - 1).getShopId();
		}

	}
}
