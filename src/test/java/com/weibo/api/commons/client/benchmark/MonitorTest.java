package com.weibo.api.commons.client.benchmark;

/**
 * 
 * Monitor test
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-22
 */

public class MonitorTest {

	private IClientTemplate iClientTemplate = IClientTemplate.mockCLientTemplate();
	
	public void testTimeout(){
		String key = "key_test";
		long sleepTime = 100000;
		long rs = iClientTemplate.mockIncAndSleep(key, sleepTime);
		Thread.currentThread().interrupt();
		System.out.println("query rs:" + rs);
	}
	
	public static void main(String[] args){
		MonitorTest monitorTest = new MonitorTest();
		monitorTest.testTimeout();
	}
}
