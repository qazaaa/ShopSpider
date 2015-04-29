package com.shopspider.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shopspider.BDB.BDBService;
import com.shopspider.BDB.SchedualSave;
import com.shopspider.bean.PageColletion;
import com.shopspider.common.Consts;
import com.shopspider.container.PageCollectionsContainer;
import com.shopspider.http.HttpService;
import com.shopspider.regex.RegexService;

@Component("initialPareseThread")
public class InitialParseThread implements Runnable, SchedualSave
{
	Logger logger = Logger.getLogger(InitialParseThread.class.getName());

	private final static String BDB_DATABSE_NAME = "initialParseThread";

	@Resource
	HttpService httpService;

	@Resource
	RegexService regexService;

	private Set<String> urlsToParse = new LinkedHashSet<String>();

	private boolean bContinue = false;

	public void setBContinue(boolean bContinue)
	{
		this.bContinue = bContinue;
	}

	private PageCollectionsContainer pageCollectionsContainer = null;

	public void SetPageCollectionsContainer(
	        PageCollectionsContainer pageCollectionsContainer)
	{
		this.pageCollectionsContainer = pageCollectionsContainer;
	}

	public InitialParseThread()
	{
	}

	public InitialParseThread(PageCollectionsContainer pageCollectionsContainer)
	{
		this.pageCollectionsContainer = pageCollectionsContainer;
	}

	@Override
	public void run()
	{
		logger.info("InitialPareseThread begin to run!");
		if (pageCollectionsContainer == null)
		{
			logger.info("The property pageCollectionsContainer is not setted, so the thread will exit");
		}

		if (!bContinue || urlsToParse == null || urlsToParse.size() == 0)
		{
			List<String> formattedCat2PageUrls = extractCat2Urls("http://s.taobao.com/search?app=shopsearch");
			if (formattedCat2PageUrls == null)
			{
				System.exit(0);
			}
			urlsToParse.addAll(formattedCat2PageUrls);
		}

		while (urlsToParse.size() > 0 && !Thread.interrupted())
		{
			List<String> operatedUrls = new ArrayList<String>(
			        urlsToParse.size());
			operatedUrls.addAll(urlsToParse);
			try
			{
				extractPageColletionFromUrls(operatedUrls);
				Thread.sleep(20000);
			} catch (InterruptedException e)
			{
				logger.info("The thread is interrupted, and thread will exit!");
				Thread.currentThread().interrupt();
			}
			logger.info("There are/is " + urlsToParse.size() + " urls to parse");
		}

	}

	private void extractPageColletionFromUrls(List<String> operatedUrls)
	        throws InterruptedException
	{
		if (operatedUrls == null || operatedUrls.size() == 0)
		{
			return;
		}
		Iterator<String> urlIter = operatedUrls.iterator();
		while (urlIter.hasNext())
		{
			String formattedPageUrl = urlIter.next();
			try
			{
				extractPageColletionFromUrl(formattedPageUrl);
			} catch (Exception e)
			{
				continue;
			}
			Boolean result = urlsToParse.remove(formattedPageUrl);
			logger.debug("Remove " + formattedPageUrl
			        + " from cat2UrlsToParse :" + result);
			Thread.sleep(Consts.SLEEP_MILLISECOND_PER_REQUEST);
		}
	}

	private void extractPageColletionFromUrl(String formattedPageUrl)
	        throws ClientProtocolException, IOException, InterruptedException
	{
		String content = httpService.getContent(formattedPageUrl);
		String totalCountStr = parseShopTotalCount(content);
		if (totalCountStr == null || totalCountStr.equals(""))
		{
			logger.error("Can't parse totalCountStr from page:"
			        + formattedPageUrl);
			return;
		}
		int totalCount = Integer.parseInt(totalCountStr);
		PageColletion pageColletionToParse = new PageColletion(
		        formattedPageUrl.replaceAll("#.*", "") + "&s=", totalCount, 0);
		pageCollectionsContainer.addPageColletion(pageColletionToParse);
		if (totalCount < Consts.MAX_SHOP_COUNT_PER_COLLECTION)
		{
			return;
		}

		// 如果当前页面的“类目”选项已经选中或者没有“类目”选项，会返回一个List，List中只有传入的formattedPageUrl
		List<String> formattedCategoryUrls = parseCategory(formattedPageUrl,
		        content);
		if (formattedCategoryUrls == null)
		{
			return;
		}

		for (String formattedCategoryUrl : formattedCategoryUrls)
		{
			parseBrandPage(formattedCategoryUrl);

		}
	}

	private void parseBrandPage(String url) throws InterruptedException
	{
		try
		{
			String content = httpService.getContent(url);
			String totalCountStr = parseShopTotalCount(content);
			if (totalCountStr == null || totalCountStr.equals(""))
			{
				logger.warn("Cant't parse total shop count in page:" + url);
				return;
			}
			int totalCount = Integer.parseInt(totalCountStr);
			pageCollectionsContainer.addPageColletion(new PageColletion(url
			        .replaceAll("#.*", "") + "&s=", totalCount, 0));
			if (totalCount < Consts.MAX_SHOP_COUNT_PER_COLLECTION)
			{
				return;
			}

			String brandContent = regexService
			        .getSingleGroupItem(
			                content,
			                "<li class=\"min\">\\s*<dl>\\s*<dt>品牌</dt>\\s*<dd>([\\s\\S]*?)</ul>\\s*</dd>\\s*</dl>");
			if (brandContent == null || brandContent.equals(""))
			{
				logger.warn("Can't parse brandContent in page :" + content);
				return;
			}

			List<String> selectedItem = regexService.getSingleGroupItems(
			        brandContent, " <a[^>]*+class=\"selected\"");
			if (selectedItem != null && selectedItem.size() > 0)
			{
				logger.info("The brand item is selected in page" + url);
				return;
			}
			List<String> brandUrls = regexService.getSingleGroupItems(
			        brandContent, " <a href=\"([^\"]++)\"");
			if (brandUrls == null || brandUrls.size() == 0)
			{
				logger.warn("Can't parse brandUrls from brandContent in page :"
				        + content);
				return;
			}
			String host = regexService.getSingleGroupItem(url,
			        "(http://[^/]++)");
			if (host == null || host == null)
			{
				logger.error("Can't parse host of page:" + url);
				return;
			}
			for (String brandUrl : brandUrls)
			{
				String formattedUrl = formattedPageUrl(host, brandUrl);
				if (formattedUrl == null || formattedUrl.equals(""))
				{
					logger.warn("Failed to formatted url:" + brandUrl);
					continue;
				}

				try
				{
					String brandPageContent = httpService
					        .getContent(formattedUrl);
					String brandPageTotalCountStr = parseShopTotalCount(brandPageContent);
					if (brandPageTotalCountStr == null
					        || brandPageTotalCountStr.equals(""))
					{
						logger.warn("Cant't parse total shop count in page:"
						        + formattedUrl);
						continue;
					}
					int brandPageTotalCount = Integer
					        .parseInt(brandPageTotalCountStr);
					pageCollectionsContainer
					        .addPageColletion(new PageColletion(formattedUrl
					                .replaceAll("#.*", "") + "&s=",
					                brandPageTotalCount, 0));
				} catch (Exception e)
				{
					logger.warn("catch exception when handle :" + formattedUrl);
					logger.warn(e.getMessage());
					continue;
				}

				Thread.sleep(Consts.SLEEP_MILLISECOND_PER_REQUEST);
			}

		} catch (InterruptedException e)
		{
			logger.warn("Catch InterruptedException when handle :" + url);
			throw e;

		} catch (Exception e)
		{
			logger.warn("catch exception when handle :" + url);
			logger.error(e.getMessage());
		}

	}

	/**
	 * 如果当前页面的“类目”已经选择，或者不存在类目，则返回一个只含有输入formattedPageUrl的List
	 * 
	 * @param formattedPageUrl
	 * @param content
	 * @return
	 */
	private List<String> parseCategory(String formattedPageUrl, String content)
	{
		String categoryUlContent = regexService
		        .getSingleGroupItem(content,
		                "<li class=\"min\">\\s*<dl>\\s*<dt>类目</dt>([\\s\\S]*?)</ul>\\s*</dd>\\s*</dl>");
		if (categoryUlContent == null || categoryUlContent.equals(""))
		{
			logger.info("Can't parse categoryUlContent or page has no category item from page:"
			        + formattedPageUrl);
			List<String> resultList = new ArrayList<String>();
			resultList.add(formattedPageUrl);
			return resultList;
		}
		List<String> selectedCategorys = regexService.getSingleGroupItems(
		        categoryUlContent, "<a[^>]*?class=\"selected\"");
		if (selectedCategorys != null && selectedCategorys.size() > 0)
		{
			logger.info("Category is selected in page:" + formattedPageUrl);
			List<String> resultList = new ArrayList<String>();
			resultList.add(formattedPageUrl);
			return resultList;
		}
		List<String> categoryUrls = regexService.getSingleGroupItems(
		        categoryUlContent, "<a href=\"([^\"]++)\"");
		if (categoryUrls == null || categoryUrls.size() == 0)
		{
			logger.error("Can't parse categoryUrls  from categoryUlContent in page:"
			        + formattedPageUrl);
			return null;
		}
		String host = regexService.getSingleGroupItem(formattedPageUrl,
		        "(http://[^/]++)");
		if (host == null || host == null)
		{
			logger.error("Can't parse host of page:" + formattedPageUrl);
			return null;
		}
		List<String> formattedCategoryUrls = new ArrayList<String>(
		        categoryUrls.size());
		for (String url : categoryUrls)
		{
			String formattedUrl = formattedPageUrl(host, url);
			if (formattedUrl == null || formattedUrl.equals(""))
			{
				logger.warn("Failed to formatted url:" + url);
				continue;

			}
			formattedCategoryUrls.add(formattedUrl);
		}
		return formattedCategoryUrls;

	}

	private String parseShopTotalCount(String content)
	{
		String totalCountStr = regexService.getSingleGroupItem(content,
		        "<span class=\"shop-count\">\\s*找到相关店铺\\s*<b>(\\d++)</b>");
		return totalCountStr;
	}

	private List<String> extractCat2Urls(String searchIndexUrl)
	{
		String searchIndexContent = null;
		try
		{
			searchIndexContent = httpService.getContent(searchIndexUrl);
		} catch (Exception e)
		{
			logger.error("Catch exception when geting searchIndexContent, and system exits");
			logger.error(e.getStackTrace());
			return null;
		}

		List<String> subSearchPages = regexService.getSingleGroupItems(
		        searchIndexContent,
		        "<ul class=\"level-two-cat-list\">([\\s\\S]*?)</ul>");
		if (subSearchPages == null || subSearchPages.size() == 0)
		{
			logger.error("Can't get subSearchPages, and system exits");
			return null;
		}
		List<String> cat2PageUrls = new ArrayList<String>();
		for (String subSearchPage : subSearchPages)
		{
			cat2PageUrls.addAll(regexService.getSingleGroupItems(subSearchPage,
			        "<li><a[^>]*?href=\"([^\"]++)\""));

		}
		List<String> formattedCat2PageUrls = new ArrayList<String>();
		String host = regexService.getSingleGroupItem(searchIndexUrl,
		        "(http://[^/]++)");
		if (host == null || host.equals(""))
		{
			logger.error("Can't get host, and system exits");
			return null;
		}
		for (String url : cat2PageUrls)
		{
			formattedCat2PageUrls.add(formattedPageUrl(host, url));
		}
		logger.info("Get " + formattedCat2PageUrls.size()
		        + " formattedCat2PageUrl");
		return formattedCat2PageUrls;
	}

	private String formattedPageUrl(String host, String url)
	{
		String resultUrl = url;
		if (url.startsWith("/search"))
		{
			resultUrl = host + resultUrl;
		}
		return resultUrl.replaceAll("&tracelog=[^&#]++", "");
	}

	@Override
	public void schedualSave(BDBService bdbService)
	{
		bdbService.put(BDB_DATABSE_NAME, "urlsToParse", urlsToParse);
	}

	@Override
	public void schedualLoad(BDBService bdbService)
	{
		@SuppressWarnings("unchecked")
		Set<String> urls = bdbService.get(BDB_DATABSE_NAME, "urlsToParse",
		        urlsToParse.getClass());
		if (urls != null)
		{
			urlsToParse = urls;
		}

	}
}
