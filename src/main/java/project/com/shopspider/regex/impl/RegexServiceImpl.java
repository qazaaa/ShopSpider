package com.shopspider.regex.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.shopspider.regex.RegexService;

@Service("regexService")
public class RegexServiceImpl implements RegexService
{
	@Override
	public List<String> getSingleGroupItems(String content, String regex)
	{
		List<String> resultList = new ArrayList<String>();

		if (content == null || regex == null)
		{
			return resultList;
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find())
		{
			resultList.add(m.group(1));
		}

		return resultList;
	}

	@Override
	public String getSingleGroupItem(String content, String regex)
	{
		if (content == null || regex == null)
		{
			return null;
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		if (m.find())
		{
			return m.group(1);
		}

		return null;
	}

	@Override
	public List<String> getMultiGroupItems(String content, String regex)
	{
		if (content == null || regex == null)
		{
			return null;
		}
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		if (m.find())
		{
			for (int i = 0; i < m.groupCount(); i++)
			{
				result.add(m.group(i + 1));
			}
		}
		return result;

	}
}
