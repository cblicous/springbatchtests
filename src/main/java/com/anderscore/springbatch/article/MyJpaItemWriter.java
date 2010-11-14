package com.anderscore.springbatch.article;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.batch.item.database.JpaItemWriter;

public class MyJpaItemWriter<T> extends JpaItemWriter<T> 
{
	@Override
	protected void doWrite(EntityManager entityManager, List<? extends T> items) 
	{
		super.doWrite(entityManager, items);
		
		System.out.println("============================");
		System.out.println("IN WRITER: (timestamp: " + new Date().getTime() + ")");
		System.out.println("Thread: " + Thread.currentThread().getName()+ " ThreadID: " + Thread.currentThread().getId());
		System.out.println("writing" + items.size() + " items");
		System.out.println("============================");
	}
	
}
