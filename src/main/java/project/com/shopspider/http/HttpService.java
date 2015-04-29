package com.shopspider.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface HttpService
{
	public String getContent(String url) throws ClientProtocolException,
	        IOException;

}
