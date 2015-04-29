package com.shopspider.container;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopspider.BDB.BDBService;
import com.shopspider.BDB.SchedualSave;
import com.shopspider.bean.ShopBean;
import com.shopspider.dao.ShopDao;

@Component("shopsContainer")
public class ShopsContainer implements SchedualSave
{
	Logger logger = Logger.getLogger(ShopsContainer.class);
	private Set<String> shopUrlSet = new HashSet<String>();

	ShopDao shopDao;

	List<ShopBean> operatedShopsList = new LinkedList<ShopBean>();

	private final static String BDB_DATABSE_NAME = "shopsContainer";

	@Autowired
	public ShopsContainer(ShopDao shopDao)
	{
		this.shopDao = shopDao;

		logger.info("begin to get shop urls which already exist");
		List<String> urls = shopDao.getAllShopUrls();
		if (urls != null && urls.size() > 0)
		{
			synchronized (shopUrlSet)
			{
				shopUrlSet.addAll(urls);
			}
		}

	}

	public void addShop(ShopBean shopBean) throws InterruptedException
	{
		synchronized (shopUrlSet)
		{
			String shopUrl = shopBean.getShopUrl().intern();
			if (shopUrlSet.contains(shopUrl))
			{
				logger.debug("Duplicate shop :" + shopUrl);
				return;
			}
			shopUrlSet.add(shopUrl);
		}
		synchronized (operatedShopsList)
		{
			operatedShopsList.add(shopBean);
		}

	}

	public void addShops(List<ShopBean> shopList) throws InterruptedException
	{
		List<ShopBean> operatedList = new LinkedList<ShopBean>();
		operatedList.addAll(shopList);
		Iterator<ShopBean> iter = operatedList.iterator();
		synchronized (shopUrlSet)
		{
			while (iter.hasNext())
			{
				String shopUrl = iter.next().getShopUrl().intern();
				if (shopUrlSet.contains(shopUrl))
				{
					logger.debug("Duplicate shop :" + shopUrl);
					iter.remove();
				} else
				{
					shopUrlSet.add(shopUrl);
				}
			}
		}
		synchronized (operatedShopsList)
		{
			operatedShopsList.addAll(operatedList);
		}

	}

	public List<ShopBean> pollShops(int dbOperateCountPerTime)
	{
		synchronized (operatedShopsList)
		{
			if (operatedShopsList.isEmpty())
			{
				return null;
			}

			int toIndex = 0;
			if (operatedShopsList.size() >= dbOperateCountPerTime)
			{
				toIndex = dbOperateCountPerTime;
			} else
			{
				toIndex = operatedShopsList.size();
			}

			List<ShopBean> result = new ArrayList<ShopBean>(toIndex);
			Iterator<ShopBean> iter = operatedShopsList.iterator();
			int i = 0;
			while (iter.hasNext())
			{
				if (++i > toIndex)
				{
					break;
				}
				ShopBean bean = iter.next();
				result.add(bean);
				iter.remove();
			}
			return result;
		}
	}

	public int getShopSize()
	{
		synchronized (operatedShopsList)
		{
			return operatedShopsList.size();
		}
	}

	@Override
	public void schedualSave(BDBService bdbService)
	{

		synchronized (shopUrlSet)
		{
			bdbService.put(BDB_DATABSE_NAME, "shopUrlSet", shopUrlSet);
		}
		synchronized (operatedShopsList)
		{
			bdbService.put(BDB_DATABSE_NAME, "operatedShopsList",
			        operatedShopsList);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void schedualLoad(BDBService bdbService)
	{
		synchronized (shopUrlSet)
		{
			Set<String> set = bdbService.get(BDB_DATABSE_NAME, "shopUrlSet",
			        shopUrlSet.getClass());
			if (set != null)
			{
				shopUrlSet.addAll(set);
			}
		}
		synchronized (operatedShopsList)
		{
			List<ShopBean> list = bdbService.get(BDB_DATABSE_NAME,
			        "operatedShopsList", operatedShopsList.getClass());
			if (list != null)
			{
				operatedShopsList = list;
			}
		}

	}
}
