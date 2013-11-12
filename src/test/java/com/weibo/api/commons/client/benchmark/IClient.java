package com.weibo.api.commons.client.benchmark;

/**
 * 
 * Client for benchmark test
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-19
 */

public interface IClient {

	long incr(String key);
	
	long mockIncAndSleep(String key, long sleepTimeMills);
}
