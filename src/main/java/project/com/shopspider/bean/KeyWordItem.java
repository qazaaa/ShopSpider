package com.shopspider.bean;

import java.io.Serializable;

public class KeyWordItem implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8265390868289632143L;

	private String keyWord;
	
	private String catName;
	
	public KeyWordItem(String keyWord, String catName) {
		this.keyWord = keyWord;
		this.catName = catName;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}
	
	

}
