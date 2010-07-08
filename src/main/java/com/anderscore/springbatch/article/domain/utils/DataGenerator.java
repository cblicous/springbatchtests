package com.anderscore.springbatch.article.domain.utils;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.anderscore.springbatch.article.domain.DetailEntity;
import com.anderscore.springbatch.article.domain.MasterEntity;

public class DataGenerator {
	 @PersistenceContext
	private EntityManager entityManager;

	 
	public EntityManager getEntityManager() {
		return entityManager;
	}


	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private String generateRandomString(int length) {

			    char[] randomString = new char[length];
			    int c  = 'A';
			    int  r1 = 0;
			    for (int i=0; i < length; i++)
			    {
			      r1 = (int)(Math.random() * 3);
			      switch(r1) {
			        case 0: c = '0' +  (int)(Math.random() * 10); break;
			        case 1: c = 'a' +  (int)(Math.random() * 26); break;
			        case 2: c = 'A' +  (int)(Math.random() * 26); break;
			      }
			      randomString[i] = (char)c;
			    }
			    return new String(randomString);
			  }

	
	private Integer generateRandomInteger(final int length) {
		return new Double(Math.random() * length).intValue();
	}
		
	public void generateData(Integer entryCount ) {
		
		for (int i = 0 ; i < entryCount; i++ ) {
			MasterEntity tempMasterEntity = new MasterEntity(); 
			BigDecimal bigDecimalField = new BigDecimal(generateRandomInteger(1000));
			tempMasterEntity.setBigDecimalField(bigDecimalField);
			tempMasterEntity.setDateField(new Date());
			tempMasterEntity.setStringField(generateRandomString(10));
			
			DetailEntity detailEntity = new DetailEntity(); 
			detailEntity.setDetailIdentifier(new Date());
			
			tempMasterEntity.setDetailEntity(detailEntity);
			
			entityManager.persist(tempMasterEntity);
			
		}
		
	}
}
