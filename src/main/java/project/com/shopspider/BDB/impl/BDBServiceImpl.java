package com.shopspider.BDB.impl;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.shopspider.BDB.BDBService;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

@Service("bDBService")
public class BDBServiceImpl implements BDBService
{
	Logger logger = Logger.getLogger(BDBServiceImpl.class);

	private Environment env = null;

	private Database catalogDatabase = null;

	private StoredClassCatalog catalog = null;

	private static final File path = new File(BDBServiceImpl.class.getResource(
	        "/").getFile()
	        + "bdb/");

	@Override
	public void initEnv(File path, EnvironmentConfig envConfig)
	{
		if (path == null)
		{
			path = BDBServiceImpl.path;
		}
		if (!path.exists())
		{
			boolean mkdirsResult = path.mkdirs();
			if (!mkdirsResult)
			{
				logger.error("Failed to make dirs: " + path.getAbsolutePath());
				return;
			}
		}
		if (envConfig == null)
		{
			envConfig = new EnvironmentConfig();
			envConfig.setTransactional(true);
			envConfig.setAllowCreate(true);
		}

		try
		{
			env = new Environment(path, envConfig);
		} catch (Exception e)
		{
			logger.error("Failed to init enviroment: " + e.getMessage());
		}

		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(true);
		dbConfig.setSortedDuplicates(false);
		try
		{
			catalogDatabase = env.openDatabase(null, "catalogDatabase",
			        dbConfig);
			catalog = new StoredClassCatalog(catalogDatabase);

		} catch (DatabaseException e)
		{
			logger.error("Failed to open catalogDatabase or create catalog,error message is: "
			        + e.getMessage());
		}

	}

	@Override
	public void closeDB()
	{
		if (env == null || catalogDatabase == null || catalog == null)
		{
			logger.error("The env or catalogDatabase or catalog is null,method will exit");
		}
		try
		{
			catalogDatabase.close();
			catalogDatabase = null;
			catalog = null;
			env.sync();
			env.close();
			env = null;
		} catch (DatabaseException e)
		{
			logger.error("Failed to close catalogDatabase or env , error message is"
			        + e.getMessage());
		}

	}

	@Override
	public <K, V> void put(String databaseName, K key, V value)
	{
		if (env == null || catalogDatabase == null || catalog == null)
		{
			logger.error("The env or catalogDatabase or catalog is null,method will exit");
			return;
		}
		if (key == null || value == null)
		{
			logger.error("The key or value is null, the method will exit");
			return;
		}
		if (databaseName == null || databaseName.equals(""))
		{
			logger.error("databaseName is null or '',method exit");
			return;
		}

		@SuppressWarnings("unchecked")
		EntryBinding<K> keyBinding = new SerialBinding<K>(catalog,
		        (Class<K>) key.getClass());
		@SuppressWarnings("unchecked")
		EntryBinding<V> valueBinding = new SerialBinding<V>(catalog,
		        (Class<V>) value.getClass());
		DatabaseEntry keyEntry = new DatabaseEntry();
		DatabaseEntry valueEntry = new DatabaseEntry();

		keyBinding.objectToEntry(key, keyEntry);
		valueBinding.objectToEntry(value, valueEntry);

		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(true);
		dbConfig.setSortedDuplicates(false);
		try
		{
			Transaction txn = env.beginTransaction(null, null);
			Database database = env.openDatabase(txn, databaseName, dbConfig);
			OperationStatus status = database.put(txn, keyEntry, valueEntry);
			txn.commit();
			if (status != OperationStatus.SUCCESS)
			{
				logger.error("Failed to save data in database: " + databaseName);
			}
			database.close();
		} catch (DatabaseException e)
		{
			logger.error("Failed to put data,error message is "
			        + e.getMessage());
		}

	}

	@Override
	public <K, V> V get(String databaseName, K key, Class<V> valueClazz)
	{
		if (env == null || catalogDatabase == null || catalog == null)
		{
			logger.error("The env or catalogDatabase or catalog is null,method will exit");
			return null;
		}
		if (key == null)
		{
			logger.error("The key is null, the method will exit");
			return null;
		}
		if (databaseName == null || databaseName.equals(""))
		{
			logger.error("databaseName is null or '',method exit");
			return null;
		}
		@SuppressWarnings("unchecked")
		EntryBinding<K> keyBinding = new SerialBinding<K>(catalog,
		        (Class<K>) key.getClass());
		@SuppressWarnings("unchecked")
		EntryBinding<V> valueBinding = new SerialBinding<V>(catalog, valueClazz);
		DatabaseEntry keyEntry = new DatabaseEntry();
		DatabaseEntry valueEntry = new DatabaseEntry();

		keyBinding.objectToEntry(key, keyEntry);

		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(true);
		dbConfig.setSortedDuplicates(false);
		try
		{
			Transaction txn = env.beginTransaction(null, null);
			Database database = env.openDatabase(txn, databaseName, dbConfig);
			OperationStatus status = database.get(txn, keyEntry, valueEntry,
			        LockMode.DEFAULT);
			txn.commit();
			database.close();
			if (status == OperationStatus.SUCCESS)
			{
				logger.debug("get succeed");
				return valueBinding.entryToObject(valueEntry);
			} else
			{
				return null;
			}

		} catch (DatabaseException e)
		{
			logger.error("Failed to put data,error message is "
			        + e.getMessage());
			return null;
		}

	}

}
