package com.anderscore.springbatch.article;

public class ResultCounter {

	public static final ResultCounter INSTANCE;
	
	static {
		INSTANCE = new ResultCounter();
	}
	
	private long counter = 0;
	
	private ResultCounter(){};
	
	synchronized void increment() {
		counter++;
	}
	
	synchronized void reset() {
		counter = 0;
	}
	
	public long numberOfResults() {
		return counter;
	}
}