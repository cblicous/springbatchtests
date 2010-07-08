package com.anderscore.springbatch.article.domain.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@ContextConfiguration(locations = { "classpath:/DataGeneratorContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})

public class DataGeneratorTest {

	@Test
	public void testGenerateData() {
		DataGenerator  dataGenerator = new DataGenerator();
		 dataGenerator.generateData(20);
	}

}
