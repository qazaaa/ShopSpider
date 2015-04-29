package com.shopspider.BDB;

import java.io.File;

import com.sleepycat.je.EnvironmentConfig;

public interface BDBService
{

	/**
	 * 
	 * @param path
	 *            若为空则使用默认路径
	 * @param envConfig
	 *            若为空则使用默认配置
	 */
	public void initEnv(File path, EnvironmentConfig envConfig);

	public void closeDB();

	public <K, V> void put(String databaseName, K key, V value);

	public <K, V> V get(String databaseName, K key, Class<V> valueClazz);

}
