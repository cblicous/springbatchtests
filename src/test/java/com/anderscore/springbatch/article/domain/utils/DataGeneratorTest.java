package com.anderscore.springbatch.article.domain.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

//@ContextConfiguration(locations = { "classpath:/DataGeneratorContext.xml" })
//@RunWith(SpringJUnit4ClassRunner.class)
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})

public class DataGeneratorTest {

	@Test
	public void testSimpleProperties() throws URISyntaxException, IOException {
		assertTrue(new File(Thread.currentThread().getContextClassLoader().getResource("DataGeneratorContext.xml").toURI()).exists());
		
	}

	@Test
	public void testGenerateData() {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] {"DataGeneratorContext.xml"});
		// of course, an ApplicationContext is just a BeanFactory

		 DataGenerator  dataGenerator = (DataGenerator) appContext.getBean("dataGenerator"); // new DataGenerator();
		 dataGenerator.generateData(20);
	}

}
