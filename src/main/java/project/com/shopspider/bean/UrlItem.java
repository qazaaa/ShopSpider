package com.shopspider.bean;

import java.io.Serializable;

public class UrlItem implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2342444802525625024L;

	private String catName;
	
	private String url;


	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
