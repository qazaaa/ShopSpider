package com.shopspider.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.shopspider.BDB.BDBService;
import com.shopspider.BDB.SchedualSave;
import com.shopspider.bean.PageColletion;
import com.shopspider.common.Consts;

public class PageCollectionsContainer implements SchedualSave
{

	transient Logger logger = Logger.getLogger(PageCollectionsContainer.class
	        .getName());

	private static final String BDB_DATABASE_NAME = "pageCollectionsContainer";

	private static final String DEFAULT_CAT_NAME = "defaultCatName";
	
	private Map<String,BlockingQueue<PageColletion>> dataQueueMap = new HashMap<String,BlockingQueue<PageColletion>>();
	
	private String curCatName = null;
	
	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public void addPageColletion(PageColletion pageColletionToParse)
	        throws InterruptedException
	{
		try
		{
			readWriteLock.readLock().lock();
			
			String catName = pageColletionToParse.getCatName();
			if (catName == null || catName.equals(""))
			{
				catName = DEFAULT_CAT_NAME;
			}
			BlockingQueue<PageColletion> dataQueue = dataQueueMap.get(catName);
			if(dataQueue == null)
			{
				dataQueue = new LinkedBlockingQueue<PageColletion>();
				dataQueueMap.put(catName, dataQueue);
			}
			if (dataQueue.size() < Consts.MAX_COUNT_PAGE_COLLECTION_QUEUE)
			{
				dataQueue.put(pageColletionToParse);
			}
		} finally
		{
			readWriteLock.readLock().unlock();
		}
	}

	public PageColletion takePageColletions() throws InterruptedException
	{
		try
		{
			readWriteLock.readLock().lock();
			int maxTry = dataQueueMap.size();
			
			for (int i = 0; i < maxTry; i++)
			{
				curCatName = this.getNextCatName();
				if (curCatName == null)
				{
					return null;
				}
				BlockingQueue<PageColletion> dataQueue = dataQueueMap.get(curCatName);
				if(dataQueue == null || dataQueue.size() == 0)
				{
					continue;
				}
				logger.info("takePageColletions for catName"+curCatName);
				return dataQueue.take();
			}
			return null;
		} finally
		{
			readWriteLock.readLock().unlock();
		}
	}
	
	private String getNextCatName()
	{
		if (dataQueueMap.keySet().size() == 0)
		{
			return null;
		}
		
		List<String> keys = new ArrayList<String>(dataQueueMap.keySet());
		if (curCatName == null)
		{
			if(keys.size() > 0 )
			{
				return keys.get(0);
			}
			return null;
		}
		
		int curIndex = keys.indexOf(curCatName);
		
		if(curIndex == -1 || curIndex == keys.size() - 1)
		{
			return keys.get(0);
		}
		
		return keys.get(curIndex + 1);
		
	}

	@Override
	public void schedualSave(BDBService bdbService)
	{
		try
		{
			readWriteLock.writeLock().lock();
			bdbService.put(BDB_DATABASE_NAME, "dataQueueMap", dataQueueMap);
		} finally
		{
			readWriteLock.writeLock().unlock();
		}
	}

	@Override
	public void schedualLoad(BDBService bdbService)
	{
		try
		{
			readWriteLock.writeLock().lock();
			@SuppressWarnings("unchecked")
			Map<String,BlockingQueue<PageColletion>> map = bdbService.get(
			        BDB_DATABASE_NAME, "dataQueueMap", dataQueueMap.getClass());
			if (map != null)
			{
				dataQueueMap = map;
			}
		} finally
		{
			readWriteLock.writeLock().unlock();
		}

	}
}
