package com.anderscore.springbatch.article;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@ContextConfiguration(locations = { "classpath:/Job.Remote.Chunking.Context.xml",
		"classpath:/jobs/Job.Remote.Chunking.Master.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class JobRemoteChunkingMasterTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job jobRemoteChunkingMaster;

	@BeforeClass
	public static void before() {
		ManageJobDB.getInstance().tearUp();
	}

	@AfterClass
	public static void after() {
		ManageJobDB.getInstance().tearDown();
		System.out.println("Number of results: "
				+ ResultCounter.INSTANCE.numberOfResults());
		ResultCounter.INSTANCE.reset();
	}

	@Test
	public void testSimpleProperties() throws URISyntaxException, IOException {
		assertTrue(new File(Thread.currentThread().getContextClassLoader().getResource("Job.Remote.Chunking.Context.xml").toURI()).exists());
		assertTrue(new File(Thread.currentThread().getContextClassLoader().getResource("jobs/Job.Remote.Chunking.Master.xml").toURI()).exists());
		assertNotNull(jobLauncher);
	}

	@Test
	public void testLaunchJob() throws Exception {
		final long begin = System.currentTimeMillis();
		JobParameters jobParams = new JobParameters();

		try {
			jobLauncher.run(jobRemoteChunkingMaster,
					jobParams);

		} finally {
			final long end = System.currentTimeMillis();
			System.out.println(this.getClass().getName()
					+ ".testLaunchJob() executed in: " + (end - begin)
					+ " ms");
		}
	}
}