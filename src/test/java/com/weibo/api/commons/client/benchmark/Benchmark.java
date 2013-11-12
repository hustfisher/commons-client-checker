package com.weibo.api.commons.client.benchmark;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 
 * Benchmark test for using commonClient and using client directly. 
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-19
 */

public class Benchmark {

	// 线上环境正常运行时的线程数与cpu核数约为50+
	private ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 50);
	
	private int taskCount = 10000000;
	private IClientTemplate iClientTemplate = null;
	private ILazyClient iLazyClient = new ILazyClient();
	
	public void testCommonClient(){
		this.iClientTemplate = IClientTemplate.mockCLientTemplate();
		
		long t1 = System.currentTimeMillis();
		for(int i = 0; i < taskCount;){
			final int keyRd = i;
			if(executor.getQueue().size() > 2000){
				TestUtil.safeSleep(50);
				continue;
			}
			executor.execute(new Runnable() {
				@Override
				public void run() {
					iClientTemplate.incr("key_" + keyRd);
				}
			});
			//inc loop index
			i++;
		}
		
		while(executor.getQueue().size() > 10){
			TestUtil.safeSleep(10);
		}
		long t2 = System.currentTimeMillis();
		
		long consumeTime = t2 - t1;
		System.out.println(String.format("CommconClient, taskCount=%s, time=%s, avg=%s", taskCount, consumeTime, taskCount/(consumeTime/1000.0)));
	}
	
	public void testClientDirectly(){
		long t1 = System.currentTimeMillis();
		for(int i = 0; i < taskCount;){
			final int keyRd = i;
			if(executor.getQueue().size() > 2000){
				TestUtil.safeSleep(50);
				continue;
			}
			executor.execute(new Runnable() {
				@Override
				public void run(){
					iLazyClient.incr("key_" + keyRd);
				}
			});
			
			//inc loop index
			i++;
		}
		
		while(executor.getQueue().size() > 10){
			TestUtil.safeSleep(10);
		}
		long t2 = System.currentTimeMillis();
		
		long consumeTime = t2 - t1;
		System.out.println(String.format("Directy Client, taskCount=%s, time=%s, avg=%s", taskCount, consumeTime, taskCount/(consumeTime/1000.0)));
	}
	
	public static void main(String[] args){
		Benchmark benchmark = new Benchmark();
		benchmark.testClientDirectly();
		TestUtil.safeSleep(1000);
		benchmark.testCommonClient();
	}
}
