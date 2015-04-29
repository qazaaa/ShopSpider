package com.shopspider.test.common;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.shopspider.common.MyStrBloomFilter;

public class MyStrBloomFilterTest
{

	@Test
	public void test()
	{
		Runtime r = Runtime.getRuntime();
		System.out.println(r.totalMemory());
		System.out.println(r.freeMemory());
		MyStrBloomFilter bloomFilter = new MyStrBloomFilter(2 << 16);
		System.out.println(r.totalMemory());
		System.out.println(r.freeMemory());
		List<String> a = new ArrayList<String>();
		a.add("asd");
		a.add("abc");
		a.add("b");
		a.add("b");
		a.add("c");
		a.add("c");
		a.add("asd");
		a.add("abc");
		a.add("ttt");
		a.add("b");
		for (String str : a)
		{
			System.out.println(str + ":" + bloomFilter.contains(str));
			if (!bloomFilter.contains(str))
			{
				bloomFilter.add(str);
			}
		}
		System.out.println("===============");
		for (int i = 0; i < 10000000; i++)
		{
			String str = Integer.toString(1);
			bloomFilter.add(str);
		}
		for (int i = 0; i < 10000; i++)
		{
			String str = Integer.toString(i);
			if (bloomFilter.contains(str))
			{
				System.out.println(str);
			} else
			{
				bloomFilter.add(str);
			}
		}

		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
