package com.shopspider.bean;

import java.io.Serializable;

import com.shopspider.common.Consts;

/**
 * @author Li
 * 
 */
public class PageColletion implements Serializable
{
	private static final long serialVersionUID = -6139793135331543857L;

	public PageColletion(String pageUrlPattern, int totalCount, int startIndex)
	{
		this.pageUrlPattern = pageUrlPattern;
		if (totalCount < Consts.MAX_INDEX_PER_COLLECTIONPAGE)
		{
			this.totalCount = totalCount;
		} else
		{
			this.totalCount = Consts.MAX_INDEX_PER_COLLECTIONPAGE;
		}

		this.currentIndex = startIndex;
		this.startIndex = startIndex;
	}

	public synchronized boolean hasNextPage()
	{
		return totalCount > 0 && (currentIndex - startIndex < totalCount);
	}

	public synchronized String getNextPage()
	{
		if (!hasNextPage())
		{
			return null;
		}
		String nextpage = pageUrlPattern + currentIndex;
		currentIndex += Consts.SHOP_COUNT_PER_PAGE;
		return nextpage;
	}

	private int totalCount = 0;
	private int currentIndex = 0;
	private int startIndex = 0;
	private String pageUrlPattern = null;

}
