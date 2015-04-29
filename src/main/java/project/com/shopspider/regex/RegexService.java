package com.shopspider.regex;

import java.util.List;

public interface RegexService
{

	List<String> getSingleGroupItems(String content, String regex);

	String getSingleGroupItem(String content, String string);

	List<String> getMultiGroupItems(String content, String regex);

}
