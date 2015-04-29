package com.shopspider.common;

import java.io.Serializable;
import java.util.BitSet;

public class MyStrBloomFilter implements Serializable
{
	private static final long serialVersionUID = -6878241669880660141L;
	private static final int DEFAULT_SIZE = 2 << 24;
	private static final int[] seeds = new int[]
	{ 7, 11, 13, 31, 37, 61 };
	private BitSet bits = new BitSet(DEFAULT_SIZE);

	private MyHash[] func = new MyHash[seeds.length];

	public MyStrBloomFilter()
	{
		init(DEFAULT_SIZE);
	}

	public MyStrBloomFilter(int size)
	{
		init(size);

	}

	private void init(int size)
	{
		if (size <= 0)
		{
			size = DEFAULT_SIZE;
		}
		bits = new BitSet(size);
		for (int i = 0; i < seeds.length; i++)
		{
			func[i] = new MyHash(size, seeds[i]);
		}
	}

	/**
	 * 可以重复add同一个值
	 * 
	 * @param str
	 */
	public void add(String str)
	{
		for (MyHash f : func)
		{
			bits.set(f.hash(str), true);
		}
	}

	public boolean contains(String str)
	{
		if (str == null)
		{
			return false;
		}
		boolean ret = true;
		for (MyHash f : func)
		{
			ret = ret && bits.get(f.hash(str));
		}
		return ret;
	}

	public static class MyHash implements Serializable
	{
		private static final long serialVersionUID = -2675152328866344952L;

		private int cap;
		private int seed;

		public MyHash(int cap, int seed)
		{
			this.cap = cap;
			this.seed = seed;
		}

		public int hash(String value)
		{
			int result = 0;
			int len = value.length();
			for (int i = 0; i < len; i++)
			{
				result = seed * result + value.charAt(i);
			}
			return (cap - 1) & result;
		}
	}
}
