/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anderscore.springbatch.article;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.batch.item.ItemProcessor;

import com.anderscore.springbatch.article.domain.AnotherEntity;
import com.anderscore.springbatch.article.domain.MasterEntity;


/**
 * processing Mock
 * 
 */
public class ToAnotherEntityProcessor implements ItemProcessor<MasterEntity, AnotherEntity>
{
	public static final BigDecimal FIXED_AMOUNT = new BigDecimal("5");

	public AnotherEntity process(MasterEntity item) throws Exception
	{
		AnotherEntity anotherEntity = new AnotherEntity();		
		
		anotherEntity.setAnotherIdentifier(item.getByteField());
		anotherEntity.setIntegerField(item.getIntegerField());
		
		ResultCounter.INSTANCE.increment();
		long i = ResultCounter.INSTANCE.numberOfResults();
//		if (i%1000 == 0)
//		{
			System.out.println("============================");
			System.out.println("IN TRANSFORMER: (timestamp: " + new Date() + ")");
			System.out.println("Thread: " + Thread.currentThread().getName()+ " ThreadID: " + Thread.currentThread().getId());
			System.out.println("Transformed items (total): " + i);
			System.out.println("============================");
//		}
		
		
		return anotherEntity;		
	}
}
