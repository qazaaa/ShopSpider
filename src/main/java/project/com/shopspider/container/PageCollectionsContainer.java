package com.shopspider.container;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.shopspider.BDB.BDBService;
import com.shopspider.BDB.SchedualSave;
import com.shopspider.bean.PageColletion;

public class PageCollectionsContainer implements SchedualSave
{

	transient Logger logger = Logger.getLogger(PageCollectionsContainer.class
	        .getName());

	private static final String BDB_DATABASE_NAME = "pageCollectionsContainer";

	private BlockingQueue<PageColletion> dataQueue = new LinkedBlockingQueue<PageColletion>();

	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public void addPageColletion(PageColletion pageColletionToParse)
	        throws InterruptedException
	{
		try
		{
			readWriteLock.readLock().lock();
			dataQueue.put(pageColletionToParse);
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
			return dataQueue.take();
		} finally
		{
			readWriteLock.readLock().unlock();
		}
	}

	@Override
	public void schedualSave(BDBService bdbService)
	{
		try
		{
			readWriteLock.writeLock().lock();
			bdbService.put(BDB_DATABASE_NAME, "dataQueue", dataQueue);
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
			BlockingQueue<PageColletion> queue = bdbService.get(
			        BDB_DATABASE_NAME, "dataQueue", dataQueue.getClass());
			if (queue != null)
			{
				dataQueue = queue;
			}
		} finally
		{
			readWriteLock.writeLock().unlock();
		}

	}
}
