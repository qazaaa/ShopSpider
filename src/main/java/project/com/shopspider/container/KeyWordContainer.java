package com.shopspider.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.shopspider.BDB.BDBService;
import com.shopspider.BDB.SchedualSave;
import com.shopspider.bean.KeyWordItem;
import com.shopspider.common.Consts;
import com.shopspider.common.MyStrBloomFilter;

public class KeyWordContainer implements SchedualSave
{

	Logger logger = Logger.getLogger(KeyWordContainer.class);

	private MyStrBloomFilter handledKeyWords = new MyStrBloomFilter(2 << 16);
	
	private Map<String, List<KeyWordItem>> availableKeyWordsMap = new HashMap<String, List<KeyWordItem>>();
	
	private String curCatName = null;
	
	private final static String DEFAULT_CAT_NAME = "default_cat_name";

	private final static String BDB_DATABSE_NAME = "keyWordContainer";

	public synchronized void addKeyWords(String keyWords, String catName)
	        throws InterruptedException
	{
		if (keyWords == null || keyWords.trim().equals(""))
		{
			return;
		}
		
		if (catName == null || catName.equals(""))
		{
			catName = DEFAULT_CAT_NAME;
		}
		
		List<KeyWordItem> keyWordList = availableKeyWordsMap.get(catName);
		
		if (keyWordList == null)
		{
			keyWordList = new LinkedList<KeyWordItem>();
			availableKeyWordsMap.put(catName, keyWordList);
		}
		
		String[] keyWordsArray = keyWords.trim().split("\\s+");
		for (String keyWord : keyWordsArray)
		{
			if (keyWordList.size() >= Consts.MAX_COUNT_KEYWORD_QUEUE)
			{
				return;
			}
			keyWord = keyWord.trim();
			if (!keyWord.equals("") || !handledKeyWords.contains(keyWord))
			{
				keyWordList.add(new KeyWordItem(keyWord, catName));
				handledKeyWords.add(keyWord);
			}
		}

	}

	public synchronized KeyWordItem pollKeyWord()
	{
		if (availableKeyWordsMap.size() == 0)
		{
			return null;
		}
		int maxTry = availableKeyWordsMap.size();
		
		for (int i = 0; i < maxTry; i++)
		{
			curCatName = this.getNextCatName();
			if (curCatName == null)
			{
				return null;
			}
			List<KeyWordItem> list = availableKeyWordsMap.get(curCatName);
			if(list == null || list.size() == 0)
			{
				continue;
			}
			KeyWordItem keyword = list.get(0);
			list.remove(0);
			return keyword;
		}

		return null;
	}
	
	private String getNextCatName()
	{
		if (availableKeyWordsMap.keySet().size() == 0)
		{
			return null;
		}
		
		List<String> keys = new ArrayList<String>(availableKeyWordsMap.keySet());
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

	public synchronized int getWordCount()
	{
		if (availableKeyWordsMap == null || availableKeyWordsMap.size() == 0)
		{
			return 0;
		}
		int count = 0;

		for (List<KeyWordItem> keywords : availableKeyWordsMap.values()) {
			count+= keywords.size();
		}

		return count;
	}

	@Override
	public synchronized void schedualSave(BDBService bdbService)
	{
		bdbService.put(BDB_DATABSE_NAME, "handledKeyWords", handledKeyWords);
		bdbService
		        .put(BDB_DATABSE_NAME, "availableKeyWordsMap", availableKeyWordsMap);

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
		Map<String, List<KeyWordItem>> map = bdbService.get(BDB_DATABSE_NAME,
		        "availableKeyWords", availableKeyWordsMap.getClass());
		if (map != null)
		{
			availableKeyWordsMap = map;
		}

	}
}
