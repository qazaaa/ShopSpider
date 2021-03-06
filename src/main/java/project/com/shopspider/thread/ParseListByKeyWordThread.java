package com.shopspider.thread;

import java.io.IOException;
import java.net.URLEncoder;

import javassist.compiler.ast.Keyword;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.shopspider.bean.KeyWordItem;
import com.shopspider.bean.PageColletion;
import com.shopspider.container.KeyWordContainer;
import com.shopspider.container.PageCollectionsContainer;
import com.shopspider.http.HttpService;
import com.shopspider.regex.RegexService;

@Component("parseListByKeyWordThread")
@Scope("prototype")
public class ParseListByKeyWordThread implements Runnable
{
	Logger logger = Logger.getLogger(ParseListByKeyWordThread.class);

	@Resource
	HttpService httpService;

	@Resource
	RegexService regexService;

	KeyWordContainer keyWordContainer = null;

	PageCollectionsContainer pageCollectionsContainer = null;

	public void setKeyWordContainer(KeyWordContainer keyWordContainer)
	{
		this.keyWordContainer = keyWordContainer;
	}

	public void setPageCollectionsContainer(
	        PageCollectionsContainer pageCollectionsContainer)
	{
		this.pageCollectionsContainer = pageCollectionsContainer;
	}

	@Override
	public void run()
	{
		if (keyWordContainer == null || pageCollectionsContainer == null)
		{
			logger.error("The property keyWordContainer or pageCollectionsContainer of class ParseListByKeyWordThread is not setted, so the thread will exit");
			return;
		}

		while (!Thread.interrupted())
		{
			KeyWordItem keyWord = keyWordContainer.pollKeyWord();
			if (keyWord == null || keyWord.getKeyWord() == null || keyWord.getKeyWord().equals(""))
			{
				continue;
			}

			try
			{
				String encodedWord = URLEncoder.encode(keyWord.getKeyWord(), "GBK");
				String url = "http://s.taobao.com/search?&app=shopsearch&q="
				        + encodedWord;
				String content = httpService.getContent(url);

				String totalCountStr = regexService
				        .getSingleGroupItem(content,
				                "<span class=\"shop-count\">[\\s\\S]*?<b>(\\d++)</b>");
				if (totalCountStr == null || totalCountStr.equals(""))
				{
					logger.info("Cant't parse total shop count in page :" + url);
					continue;
				}
				int totalCount = Integer.parseInt(totalCountStr);
				PageColletion collection = new PageColletion(url + "&s=",
				        totalCount, 0, keyWord.getCatName());
				pageCollectionsContainer.addPageColletion(collection);
				logger.info("Add key word page :" + url);

				Thread.sleep(30000);

			} catch (InterruptedException e)
			{
				logger.info("Thread is interrupted, and will exit");
				Thread.currentThread().interrupt();
			} catch (IOException e)
			{
				logger.debug(e.getMessage());
				continue;
			}
		}
	}
}
