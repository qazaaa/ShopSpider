package com.shopspider.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopspider.bean.ShopBean;
import com.shopspider.dao.ShopDao;

@Service("shopDaoImpl")
public class ShopDaoImpl implements ShopDao
{
	Logger logger = Logger.getLogger(ShopDaoImpl.class);

	@Resource
	SessionFactory sessionFactory;

	@Override
	@Transactional
	public void saveAll(List<ShopBean> shopsList)
	{
		Session session = sessionFactory.getCurrentSession();

		for (int i = 0; i < shopsList.size(); i++)
		{
			ShopBean shopBean = shopsList.get(i);
			session.save(shopBean);
			
			if (i % 20 == 0)
			{
				session.flush();
				session.clear();
			}
		}
		session.flush();
		session.clear();

	}

	@Override
	@Transactional
	public void add(ShopBean bean)
	{
		Session session = sessionFactory.getCurrentSession();
		session.save(bean);
		System.out.println("aaa");
	}

	@Override
	@Transactional
	public ShopBean queryOneShop(int id)
	{
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from ShopBean where id=:id");
		q.setInteger("id", id);
		List<ShopBean> l = (List<ShopBean>) q.list();
		if (l.size() > 0)
		{
			return l.get(0);
		} else
		{
			return null;
		}

	}

	@Override
	@Transactional
	public List<ShopBean> queryShops(String whrereStr, int start, int maxResults)
	{
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from ShopBean "
		        + (whrereStr == null ? "" : whrereStr));
		q.setFirstResult(start);
		q.setMaxResults(maxResults);
		return (List<ShopBean>) q.list();
	}

	@Override
	@Transactional
	public void updateAll(List<ShopBean> shops)
	{
		Session session = sessionFactory.getCurrentSession();
		for (int i = 0; i < shops.size(); i++)
		{
			session.update(shops.get(i));
			if (i % 20 == 0)
			{
				session.flush();
				session.clear();
			}
		}
		session.flush();
		session.clear();
	}

	@Override
	@Transactional
	public List<ShopBean> getAllShopsOrderByUrl()
	{
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from ShopBean ORDER BY shop_url");
		return (List<ShopBean>) q.list();
	}

	@Override
	@Transactional
	public void deleteAll(List<ShopBean> shops)
	{
		Session session = sessionFactory.getCurrentSession();
		for (ShopBean shopBean : shops)
		{
			session.delete(shopBean);
		}

	}

	@Override
	@Transactional
	public List<String> getAllShopUrls()
	{
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("select shopId from ShopBean ");
		return (List<String>) q.list();

	}
}
