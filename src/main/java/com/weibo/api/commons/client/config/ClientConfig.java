package com.weibo.api.commons.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weibo.api.commons.client.util.ClientConstants;

/**
 * 
 * Common configuration for all clients. 
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */

public class ClientConfig {

	private static Logger log = LoggerFactory.getLogger(ClientConfig.class);
	
	/** client operation的超时时间，默认是5000 mills。由于timeout是通过外部deamon线程轮询监测，每100ms执行一轮，所以此处的timeout不能小于100ms */
	private int timeoutMills = 5000;
	
	/** client retries count, default 0 means not retry */
	private int retries = 0;
	
	/**
	 * set timeout in mills. Ignore the timeoutMills param if it is less than #ClientConstants.TIMEOUT_THRESHOLD_MILLS
	 * @param timeoutMills
	 */
	public void setTimeoutMills(int timeoutMills) {
		if(timeoutMills > ClientConstants.TIMEOUT_THRESHOLD_MILLS){
			this.timeoutMills = timeoutMills;
		}else{
			log.warn(String.format("Ignore ClientCofig.timeoutMills:%s, minValue=%s", timeoutMills, ClientConstants.TIMEOUT_THRESHOLD_MILLS));
		}
	}
	
	public int getTimeoutMills() {
		return timeoutMills;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		if(retries > 0){
			this.retries = retries;
		}
	}
	
}
