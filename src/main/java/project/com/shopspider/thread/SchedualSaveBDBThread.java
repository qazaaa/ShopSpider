package com.shopspider.thread;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.shopspider.BDB.BDBService;
import com.shopspider.container.KeyWordContainer;
import com.shopspider.container.PageCollectionsContainer;
import com.shopspider.container.ShopsContainer;
import com.shopspider.main.FetchShops;

@Component("schedualSaveBDBThread")
public class SchedualSaveBDBThread implements Runnable
{
	Logger logger = Logger.getLogger(SchedualSaveBDBThread.class);

	PageCollectionsContainer pageCollectionsContainer = null;
	ShopsContainer shopsContainer = null;
	KeyWordContainer keyWordContainer = null;
	InitialParseThread initialParseThreads = null;
	List<ParseListByKeyWordThread> parseListByKeyWordThreads = new ArrayList<ParseListByKeyWordThread>();
	List<ParseShopListPageThread> parseShopListPageThreads = new ArrayList<ParseShopListPageThread>();
	List<ShopDBThread> shopDBThreads = new ArrayList<ShopDBThread>();

	@Resource
	BDBService bdbService;

	@Resource
	JavaMailSender mailSender;

	@Override
	public void run()
	{
		logger.info("Begin to save data in BDB");
		bdbService.initEnv(null, null);

		pageCollectionsContainer.schedualSave(bdbService);
		shopsContainer.schedualSave(bdbService);
		keyWordContainer.schedualSave(bdbService);
		initialParseThreads.schedualSave(bdbService);

		bdbService.closeDB();

		logger.info("Save data in BDB successfully");
		try
		{
			sendEmail();
		} catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendEmail() throws MessagingException
	{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom("shopspider@163.com");
		helper.setTo("shopspider@163.com");
		helper.setSubject("From shopspider");
		helper.setText("Everything is ok");
		FileSystemResource log = new FileSystemResource(FetchShops.class
		        .getResource("/").getFile() + "/logs/shopspider.log");
		helper.addAttachment("log.txt", log);
		mailSender.send(message);
	}

	public void load()
	{
		logger.info("Begin to load data in BDB");
		bdbService.initEnv(null, null);

		pageCollectionsContainer.schedualLoad(bdbService);
		shopsContainer.schedualLoad(bdbService);
		keyWordContainer.schedualLoad(bdbService);
		initialParseThreads.schedualLoad(bdbService);

		bdbService.closeDB();
		logger.info("Load data in BDB successfully");
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

	public void setInitialParseThread(InitialParseThread thread)
	{
		initialParseThreads = thread;
	}

	public void addParseListByKeyWordThread(ParseListByKeyWordThread thread)
	{
		if (parseListByKeyWordThreads != null)
		{
			parseListByKeyWordThreads.add(thread);
		}
	}

	public void addParseShopListPageThread(ParseShopListPageThread thread)
	{
		if (parseShopListPageThreads != null)
		{
			parseShopListPageThreads.add(thread);
		}
	}

	public void addShopDBThread(ShopDBThread thread)
	{
		if (shopDBThreads != null)
		{
			shopDBThreads.add(thread);
		}
	}

}
