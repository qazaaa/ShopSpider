package com.shopspider.http.impl;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;



import com.shopspider.http.HttpService;

@Service("httpServiceImpl")
public class HttpServiceImpl implements HttpService
{
	static Logger logger = Logger.getLogger(HttpServiceImpl.class.getName());

	@Override
	public String getContent(String url) throws ClientProtocolException,
	        IOException
	{
		logger.debug("getContent:" + url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		
		HttpResponse response = httpClient.execute(httpGet);
		logger.debug("end of getContent:" + url);
		return EntityUtils.toString(response.getEntity(), "GBK");
	}

}
