package com.weibo.api.commons.client.benchmark;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.weibo.api.commons.client.exception.ServerUnavailableException;

/**
 * 
 * To sleep when do query.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-19
 */

public class ILazyClient implements IClient{

	@Override
	public long incr(String key){
		int rd = (int)(Math.random() * 10000);
		if(rd > 9990){
			TestUtil.safeSleep(rd % 1000);
		}else{
			TestUtil.safeSleep(rd % 10);
		}
		return rd;
	}
	
	@Override
	public long mockIncAndSleep(String key, long sleepTimeMills){
		if(sleepTimeMills > 1000){
			if(Math.random() > 0.5){
				throw new ClientTimeoutException("timeout in mockInc");
			}
			TestUtil.safeSleep(sleepTimeMills);
		}
		return incr(key);
	}
}
