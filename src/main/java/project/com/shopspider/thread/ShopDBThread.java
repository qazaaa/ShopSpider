package com.shopspider.thread;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shopspider.bean.ShopBean;
import com.shopspider.common.Consts;
import com.shopspider.container.ShopsContainer;
import com.shopspider.dao.ShopDao;

@Component("shopDBThread")
public class ShopDBThread implements Runnable
{
	Logger logger = Logger.getLogger(ShopDBThread.class);

	@Resource
	ShopDao shopDao;

	ShopsContainer shopsContainer = null;

	public void setShopsContainer(ShopsContainer shopsContainer)
	{
		this.shopsContainer = shopsContainer;
	}

	@Override
	public void run()
	{
		if (shopsContainer == null)
		{
			logger.error("The property shopsContainer of class ShopDBTread is null,the ShopDBTread will exit!");
			return;
		}
		while (!Thread.interrupted())
		{
			List<ShopBean> operatedList = shopsContainer
			        .pollShops(Consts.DB_OPERATE_COUNT_PER_TIME);
			if (operatedList != null)
			{
				logger.info("ShopDBTread get " + operatedList.size()
				        + " shops to operate!");
				shopDao.saveAll(operatedList);

			} else
			{
				logger.info("ShopDBTread get no shops to operate!");
			}

			try
			{
				Thread.sleep(Consts.SHOP_DB_THREAD_SLEEP_MILLISECOND);
			} catch (InterruptedException e)
			{
				logger.info("Catch InterruptedException,the thread will exit");
				Thread.currentThread().interrupt();
			}
		}

	}

}
