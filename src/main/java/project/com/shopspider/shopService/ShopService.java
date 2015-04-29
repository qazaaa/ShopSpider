package com.shopspider.shopService;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.shopspider.bean.ShopBean;

public interface ShopService
{

	ShopBean parseShopFromList(String shopContent);

	ShopBean updateShopDetail(ShopBean shopBean)
	        throws ClientProtocolException, IOException;

}
