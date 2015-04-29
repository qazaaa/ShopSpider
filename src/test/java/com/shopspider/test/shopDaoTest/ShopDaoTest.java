package com.shopspider.test.shopDaoTest;

import java.util.Date;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.shopspider.bean.ShopBean;
import com.shopspider.bean.ShopType;
import com.shopspider.dao.ShopDao;
import com.shopspider.main.FetchShops;

public class ShopDaoTest
{

	@Test
	public void test()
	{
		ShopBean bean = new ShopBean();
		bean.setShopName("小女人时尚服饰屋");
		bean.setSellerName("小女人时尚服饰屋");
		bean.setShopUrl("http://shop72097388.taobao.com/?spm=0.0.0.0.JFyvQK");
		bean.setLocation("江苏 泰州");
		bean.setSaleCount(248);
		bean.setProductCount(170);
		bean.setProductCount(21);
		bean.setNewProductCount(19);
		bean.setPraiseRate(97.16);
		bean.setServices("7天退货");
		bean.setDescriptionRight(4.63);
		bean.setDescriptionRightCompared(-8.79);
		bean.setServiceAttitude(4.33);
		bean.setServiceAttitudeCompared(-10.13);
		bean.setDeliverySpeed(4.17);
		bean.setDeliverySpeedCompared(-13.11);
		bean.setShopRateUrl("http://rate.taobao.com/user-rate-739690901.htm?spm=0.0.0.0.JFyvQK");
		// bean.setScoreCount(6);
		bean.setPraiseScore(4.16667);
		// bean.setScore1Rate(16.67);
		// bean.setScore2Rate(0);
		// bean.setScore3Rate(0);
		// bean.setScore4Rate(16.67);
		// bean.setScore5Rate(66.67);
		bean.setAverageRefundSpeed(0);
		bean.setAverageRefundSpeedOfBussiness(3.70);
		bean.setRefundRate(0);
		bean.setRefundRateOfBussiness(10.86);
		bean.setComplaintRate(0);
		bean.setComplaintRateOfBussiness(0.01);
		// bean.setPunishCount(0);
		// bean.setPunishRateOfBussiness(0.60);
		bean.setCreatedAt(new Date());
		bean.setUpdatedAt(new Date());
		bean.setMainBusiness("服饰鞋包");
		bean.setMainBusinessRate(100);
		bean.setEstablishAt(new Date());
		bean.setShopSellerCreditLevel(6);
		bean.setShopSellerCreditValue(336);
		bean.setShopSellerCreditPic("http://pics.taobaocdn.com/newrank/s_blue_1.gif");
		bean.setShopBuyerCreditValue(38);
		bean.setShopBuyerCreditLevel(2);
		bean.setShopBuyerCreditPic("http://pics.taobaocdn.com/newrank/b_red_2.gif");
		bean.setShopType(ShopType.Taobao);

		ApplicationContext context = new FileSystemXmlApplicationContext(
		        FetchShops.class.getResource("/").getFile() + "beans.xml");
		ShopDao shopDao = context.getBean(ShopDao.class);

		shopDao.add(bean);

		// List<ShopBean> shopsList = new ArrayList<ShopBean>();
		// shopsList.add(bean);
		// shopDao.saveAll(shopsList);

	}

	@Test
	public void testQueryOne()
	{
		// ApplicationContext context = new FileSystemXmlApplicationContext(
		// FetchShops.class.getResource("/").getFile() + "beans.xml");
		// ShopDao shopDao = context.getBean(ShopDao.class);
		// ShopBean bean = shopDao.queryOneShop(7);
		// System.out.println(bean.getShopName());
		// System.out.println(bean.getShopType());
	}
}
