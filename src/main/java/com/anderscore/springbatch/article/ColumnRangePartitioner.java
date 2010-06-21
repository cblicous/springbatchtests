package com.anderscore.springbatch.article;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
 
public class ColumnRangePartitioner implements Partitioner
{
	private SimpleJdbcTemplate jdbcTemplate;
 
	private String table;
 
	private String column;
 	
	private Connection con;
	
	public ColumnRangePartitioner()
	{
		InputStream propInFile = ColumnRangePartitioner.class.getClassLoader().getResourceAsStream("batch.properties");
	    Properties p = new Properties();
	    try 
	    {
			p.load( propInFile );
			
			String jdbcDriver = p.getProperty("batch.jdbc.driver");
			String jdbcUrl = p.getProperty("batch.jdbc.url");
			String jdbcUser = p.getProperty("batch.jdbc.user");
			String jdbcPass = p.getProperty("batch.jdbc.password");
			
//			System.out.println(jdbcDriver);
//			System.out.println(jdbcUrl);
//			System.out.println(jdbcUser);
//			System.out.println(jdbcPass);
			
			Class.forName(jdbcDriver);
			
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL(jdbcUrl);
			ds.setUser(jdbcUser);
			ds.setPassword(jdbcPass);
//			ds.getConnection(jdbcUser, jdbcPass);
			ds.getConnection();
			
			
			jdbcTemplate = new SimpleJdbcTemplate(ds);
			
	    }
	    catch (IOException e1)
	    {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	    catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
	/**
	 * The name of the SQL table the data are in.
	 * 
	 * @param table the name of the table
	 */
	public void setTable(String table) {
		this.table = table;
	}
 
	/**
	 * The name of the column to partition.
	 * 
	 * @param column the column name.
	 */
	public void setColumn(String column) {
		this.column = column;
	}
 
	/**
	 * Partition a database table assuming that the data in the column specified
	 * are uniformly distributed. The execution context values will have keys
	 * <code>minValue</code> and <code>maxValue</code> specifying the range of
	 * values to consider in each partition.
	 * 
	 * @see Partitioner#partition(int)
	 */
	public Map<String, ExecutionContext> partition(int gridSize) 
	{
		System.out.println("GridSize: " + gridSize);
		
		System.out.println("in partition method");
		
		long min = jdbcTemplate.queryForInt("SELECT MIN(" + column + ") from " + table);
		long max = jdbcTemplate.queryForInt("SELECT MAX(" + column + ") from " + table);
		
		long partitionSize = (max-min) / gridSize + 1;
		System.out.println("partitionSize: " + partitionSize);
		
		System.out.println("min"+min);
		System.out.println("max"+max);
		
		Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
		long number = 0;
		long start = min;
		long end = start + partitionSize - 1;
 
		while (start <= max) 
		{
			ExecutionContext value = new ExecutionContext();			
			System.out.println("partition" + number);
			if (end >= max) {
				end = max;
			}
			value.putLong("minValue", start);
			value.putLong("maxValue", end);
			//value.putString("outputFile", "file:./target/output/" + ("outputFile"+ number) + ".csv");
			System.out.println("minValue"+start);
			System.out.println("maxValue"+end);			
			result.put("partition" + number, value);
			start += partitionSize;
			end += partitionSize;
			number++;
		}
		return result;
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

