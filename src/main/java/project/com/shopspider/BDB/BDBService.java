package com.shopspider.BDB;

import java.io.File;

import com.sleepycat.je.EnvironmentConfig;

public interface BDBService
{

	/**
	 * 
	 * @param path
	 *            ��Ϊ����ʹ��Ĭ��·��
	 * @param envConfig
	 *            ��Ϊ����ʹ��Ĭ������
	 */
	public void initEnv(File path, EnvironmentConfig envConfig);

	public void closeDB();

	public <K, V> void put(String databaseName, K key, V value);

	public <K, V> V get(String databaseName, K key, Class<V> valueClazz);

}
