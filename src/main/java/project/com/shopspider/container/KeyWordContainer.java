package com.shopspider.container;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.shopspider.BDB.BDBService;
import com.shopspider.BDB.SchedualSave;
import com.shopspider.common.Consts;
import com.shopspider.common.MyStrBloomFilter;

public class KeyWordContainer implements SchedualSave
{

	Logger logger = Logger.getLogger(KeyWordContainer.class);

	private MyStrBloomFilter handledKeyWords = new MyStrBloomFilter(2 << 16);

	private List<String> availableKeyWords = new LinkedList<String>();

	private final static String BDB_DATABSE_NAME = "keyWordContainer";

	public synchronized void addKeyWords(String keyWords)
	        throws InterruptedException
	{
		if (keyWords == null || keyWords.trim().equals(""))
		{
			return;
		}
		String[] keyWordsArray = keyWords.trim().split("\\s+");
		for (String keyWord : keyWordsArray)
		{
			if (availableKeyWords.size() >= Consts.MAX_COUNT_KEYWORD_QUEUE)
			{
				return;
			}
			keyWord = keyWord.trim();
			if (!keyWord.equals("") || !handledKeyWords.contains(keyWord))
			{
				availableKeyWords.add(keyWord);
				handledKeyWords.add(keyWord);
			}
		}

	}

	public synchronized String pollKeyWord()
	{
		if (availableKeyWords.size() == 0)
		{
			return null;
		}
		String word = availableKeyWords.get(0);
		availableKeyWords.remove(0);
		return word;
	}

	public synchronized int getWordCount()
	{
		return availableKeyWords.size();
	}

	@Override
	public synchronized void schedualSave(BDBService bdbService)
	{
		bdbService.put(BDB_DATABSE_NAME, "handledKeyWords", handledKeyWords);
		bdbService
		        .put(BDB_DATABSE_NAME, "availableKeyWords", availableKeyWords);

	}

	@Override
	public synchronized void schedualLoad(BDBService bdbService)
	{
		MyStrBloomFilter handledWords = bdbService.get(BDB_DATABSE_NAME,
		        "handledKeyWords", handledKeyWords.getClass());
		if (handledWords != null)
		{
			handledKeyWords = handledWords;
		}

		@SuppressWarnings("unchecked")
		List<String> availableWords = bdbService.get(BDB_DATABSE_NAME,
		        "availableKeyWords", availableKeyWords.getClass());
		if (availableWords != null)
		{
			availableKeyWords = availableWords;
		}

	}
}
