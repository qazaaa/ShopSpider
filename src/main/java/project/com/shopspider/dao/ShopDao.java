package com.shopspider.dao;

import java.util.List;

import com.shopspider.bean.ShopBean;

public interface ShopDao
{

	void saveAll(List<ShopBean> shopsList);

	void add(ShopBean bean);

	ShopBean queryOneShop(int id);

	void updateAll(List<ShopBean> shops);

	List<ShopBean> queryShops(String whrereStr, int start, int maxResults);

	List<ShopBean> getAllShopsOrderByUrl();

	void deleteAll(List<ShopBean> shopsToDelete);

	List<String> getAllShopUrls();

}
