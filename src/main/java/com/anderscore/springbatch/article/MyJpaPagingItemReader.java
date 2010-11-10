package com.anderscore.springbatch.article;
import java.util.Date;

import org.springframework.batch.item.database.JpaPagingItemReader;


public class MyJpaPagingItemReader<T> extends JpaPagingItemReader<T> 
{
	@Override
	public void setQueryString(String queryString)
	{
		super.setQueryString(queryString);
		System.out.println("============================");
		System.out.println("IN READER: (timestamp: " + new Date() + ")");
		System.out.println("Thread: " + Thread.currentThread().getName()+ " ThreadID: " + Thread.currentThread().getId());
		System.out.println("Set JpaQueryString to:" + queryString);
		System.out.println("============================");
	}
	
	 @Override
	 protected void doReadPage() 
	 {
		 super.doReadPage();
		 System.out.println("============================");
		 System.out.println("IN READER: (timestamp: " + new Date() + ")");
		 System.out.println("Thread: " + Thread.currentThread().getName()+ " ThreadID: " + Thread.currentThread().getId());
		 System.out.println("JpaItemReader read a page.");
		 System.out.println("read " + super.results.size() + " items.");
		 System.out.println("===============");
	 }
}
