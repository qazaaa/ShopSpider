package com.shopspider.thread;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.shopspider.bean.PageColletion;
import com.shopspider.bean.ShopBean;
import com.shopspider.common.Consts;
import com.shopspider.container.KeyWordContainer;
import com.shopspider.container.PageCollectionsContainer;
import com.shopspider.container.ShopsContainer;
import com.shopspider.http.HttpService;
import com.shopspider.regex.RegexService;
import com.shopspider.shopService.ShopService;

@Component("parseShopListPageThread")
@Scope("prototype")
public class ParseShopListPageThread implements Runnable
{
	Logger logger = Logger.getLogger(ParseShopListPageThread.class);

	private PageCollectionsContainer pageCollectionsContainer = null;

	private ShopsContainer shopsContainer = null;

	private KeyWordContainer keyWordContainer = null;

	@Resource
	private HttpService httpService;

	@Resource
	private RegexService regexService;

	@Resource
	private ShopService shopService;

	public ParseShopListPageThread()
	{
		logger.debug("new GenerateShopListPageThread generated");
	}

	public void setPageCollectionsContainer(
	        PageCollectionsContainer pageCollectionsContainer)
	{
		this.pageCollectionsContainer = pageCollectionsContainer;
	}

	public void setShopsContainer(ShopsContainer shopsContainer)
	{
		this.shopsContainer = shopsContainer;
	}

	public void setKeyWordContainer(KeyWordContainer keyWordContainer)
	{
		this.keyWordContainer = keyWordContainer;
	}

	@Override
	public void run()
	{
		if (pageCollectionsContainer == null || shopsContainer == null
		        || keyWordContainer == null)
		{
			logger.error("The property pageCollectionsContainer or shopsContainer or keyWordContainer of class ParseShopListPageThread is not setted,so the ParseShopListPageThread will be existed");
			return;
		}
		while (!Thread.interrupted())
		{
			PageColletion pageColletion = null;
			try
			{
				pageColletion = pageCollectionsContainer.takePageColletions();
				if (pageColletion == null)
				{
					Thread.sleep(20000);
					continue;
				}
			} catch (InterruptedException e)
			{
				logger.info("The thread is interrupted and will exit");
				Thread.currentThread().interrupt();
			}

			while (pageColletion.hasNextPage())
			{
				String pageUrl = pageColletion.getNextPage();
				logger.debug("Begin to parse page:" + pageUrl);
				String pageContent = null;
				try
				{
					pageContent = httpService.getContent(pageUrl);
				} catch (IOException e)
				{
					logger.info(e.getMessage());
				}
				if (pageContent == null || pageContent.equals(""))
				{
					logger.info("Catch exception when get url :" + pageUrl);
					continue;
				}

				List<String> shopContentList = regexService
				        .getSingleGroupItems(pageContent,
				                "<li class=\"list-item\">([\\s\\S]*?)<p class=\"item-bottom\">");
				try
				{
					for (String shopContent : shopContentList)
					{
						ShopBean shopBean = shopService
						        .parseShopFromList(shopContent);
						shopsContainer.addShop(shopBean);
						String business = shopBean.getBusiness();
						if (business != null && !business.trim().equals(""))
						{
							keyWordContainer.addKeyWords(business.trim(),pageColletion.getCatName());
						}
					}

					Thread.sleep(Consts.SLEEP_MILLISECOND_PER_REQUEST);

				} catch (InterruptedException e)
				{
					logger.info("The thread is interrupted and will exit");
					Thread.currentThread().interrupt();
				}

				logger.info("There are/is " + shopsContainer.getShopSize()
				        + " shops in shopsContainer");
			}

		}

	}
}
