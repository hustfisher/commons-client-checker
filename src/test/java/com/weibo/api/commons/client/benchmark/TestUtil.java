package com.weibo.api.commons.client.benchmark;

/**
 * 
 * Test utils.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-22
 */

public class TestUtil {

	public static void safeSleep(long timeMills){
		try {
			Thread.sleep(timeMills);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
