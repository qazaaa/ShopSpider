package com.shopspider.common;

public class Consts
{
	public static final int MAX_SHOP_COUNT_PER_COLLECTION = 10000;
	public static final int SHOP_COUNT_PER_PAGE = 20;
	public static final int NEW_PRODUCT_COUNT_20_PLUS = 21;
	public static final int PROMOTION_COUNT_20_PLUS = 21;

	public static final int SHOP_CONTAINER_CAPACITY = 100000;

	/**
	 * db线程每次处理数据上限
	 */
	public static final int DB_OPERATE_COUNT_PER_TIME = 3000;

	/**
	 * db线程每隔这么久去获取数据
	 */
	public static final long SHOP_DB_THREAD_SLEEP_MILLISECOND = 10000; //120000

	public static final long SLEEP_MILLISECOND_PER_REQUEST = 10000;

	/**
	 * &s=10220 中s的最大值
	 */
	public static final int MAX_INDEX_PER_COLLECTIONPAGE = 3000;

	// 定时保存的时间间隔
	public static final long SCHEDUALE_SAVE_INTERVAL_IN_SECOND = 60 * 30;
	public static int MAX_COUNT_KEYWORD_QUEUE = 5000000;

}
