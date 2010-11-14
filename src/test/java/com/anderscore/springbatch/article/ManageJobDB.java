package com.anderscore.springbatch.article;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ManageJobDB
{
	private Properties p;
	private Connection con;
	
	private static ManageJobDB instance;
	
	public synchronized static ManageJobDB getInstance()
	{
		if(instance == null)
			instance = new ManageJobDB();
		
		return instance;
	}
	
	public Properties getProperties()
	{
		return p;
	}
	
	public Connection getConnection()
	{
		return con;
	}
		
	private ManageJobDB()
	{
		try
		{
			InputStream propInFile = ManageJobDB.class.getClassLoader().getResourceAsStream("batch.properties");
		
		    p = new Properties();
		    p.load( propInFile );
		}
	    catch (FileNotFoundException e2)
	    {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String jdbcDriver = p.getProperty("batch.jdbc.driver");
		String jdbcUrl = p.getProperty("batch.jdbc.url");
		String jdbcUser = p.getProperty("batch.jdbc.user");
		String jdbcPass = p.getProperty("batch.jdbc.password");
			
//		con = DriverManager.getConnection("jdbc:oracle:thin:conagis/conagis@//localhost:1521/XE");
		try
		{
			Class.forName(jdbcDriver);
			con = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Sets up the Job-Execution tables etc for Spring batch
	 * 
	 */
	public void tearDown()
	{
		try
		{
			con.prepareStatement("DROP TABLE  BATCH_STEP_EXECUTION_CONTEXT").execute();
			con.prepareStatement("DROP TABLE  BATCH_JOB_EXECUTION_CONTEXT").execute();
			con.prepareStatement("DROP TABLE  BATCH_STEP_EXECUTION").execute();
			con.prepareStatement("DROP TABLE  BATCH_JOB_EXECUTION").execute();
			con.prepareStatement("DROP TABLE  BATCH_JOB_PARAMS").execute();
			con.prepareStatement("DROP TABLE  BATCH_JOB_INSTANCE").execute();
			
			con.prepareStatement("DROP SEQUENCE  BATCH_STEP_EXECUTION_SEQ").execute();
			con.prepareStatement("DROP SEQUENCE  BATCH_JOB_EXECUTION_SEQ").execute();
			con.prepareStatement("DROP SEQUENCE  BATCH_JOB_SEQ").execute();
			
//			con.prepareStatement("TRUNCATE TABLE AB_AUF_AUFTRAG_S").execute();
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	
	

	
	
	/** Tears down Job tables 
	 * 
	 */
	public void tearUp()
	{
		try
		{	
			con.prepareStatement("CREATE TABLE BATCH_JOB_INSTANCE  " +
					"(JOB_INSTANCE_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ," +
					"  VERSION NUMBER(19,0) ," +
					"  JOB_NAME VARCHAR2(100) NOT NULL," +
					" JOB_KEY VARCHAR2(32) NOT NULL," +
					" constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY))").execute();
	
			con.prepareStatement("CREATE TABLE BATCH_JOB_EXECUTION  (JOB_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ," +
					" VERSION NUMBER(19,0)  ," +
					" JOB_INSTANCE_ID NUMBER(19,0) NOT NULL," +
					" CREATE_TIME TIMESTAMP NOT NULL," +
					" START_TIME TIMESTAMP DEFAULT NULL ," +
					" END_TIME TIMESTAMP DEFAULT NULL ," +
					" STATUS VARCHAR2(10) ," +
					" EXIT_CODE VARCHAR2(20) ," +
					" EXIT_MESSAGE VARCHAR2(2500) ," +
					" LAST_UPDATED TIMESTAMP," +
					" constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID) references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID))").execute(); 
				
			con.prepareStatement("CREATE TABLE BATCH_JOB_PARAMS  (JOB_INSTANCE_ID NUMBER(19,0) NOT NULL ," +
					" TYPE_CD VARCHAR2(6) NOT NULL ," +
					" KEY_NAME VARCHAR2(100) NOT NULL ," +
					" STRING_VAL VARCHAR2(250) ," +
					" DATE_VAL TIMESTAMP DEFAULT NULL ," +
					" LONG_VAL NUMBER(19,0) ," +
					" DOUBLE_VAL NUMBER ," +
					" constraint JOB_INST_PARAMS_FK foreign key (JOB_INSTANCE_ID) references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID))").execute();
				
			con.prepareStatement("CREATE TABLE BATCH_STEP_EXECUTION  (STEP_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ," +
					" VERSION NUMBER(19,0) NOT NULL," +
					" STEP_NAME VARCHAR2(100) NOT NULL," +
					" JOB_EXECUTION_ID NUMBER(19,0) NOT NULL," +
					"START_TIME TIMESTAMP NOT NULL ," +
					" END_TIME TIMESTAMP DEFAULT NULL ," +
					" STATUS VARCHAR2(10) ," +
					"COMMIT_COUNT NUMBER(19,0) ," +
					" READ_COUNT NUMBER(19,0) ," +
					" FILTER_COUNT NUMBER(19,0) ," +
					"WRITE_COUNT NUMBER(19,0) ," +
					"READ_SKIP_COUNT NUMBER(19,0) ," +
					"WRITE_SKIP_COUNT NUMBER(19,0) ," +
					"PROCESS_SKIP_COUNT NUMBER(19,0) ," +
					"ROLLBACK_COUNT NUMBER(19,0) ," +
					" EXIT_CODE VARCHAR2(20) ," +
					"EXIT_MESSAGE VARCHAR2(2500) ," +
					"LAST_UPDATED TIMESTAMP," +
					"constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID))").execute();
	
			con.prepareStatement("CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (STEP_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY," +
					"SHORT_CONTEXT VARCHAR2(2500) NOT NULL," +
					"SERIALIZED_CONTEXT CLOB ," +
					" constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID) references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID))").execute();
	
			con.prepareStatement("CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT (JOB_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY," +
					"SHORT_CONTEXT VARCHAR2(2500) NOT NULL," +
					"SERIALIZED_CONTEXT CLOB ," +
					" constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID))").execute();
	
			con.prepareStatement("CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NOCYCLE").execute();
			con.prepareStatement("CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NOCYCLE").execute();
			con.prepareStatement("CREATE SEQUENCE BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NOCYCLE").execute();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		} 
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		if(con != null)
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
