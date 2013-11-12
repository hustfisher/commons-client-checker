package com.weibo.api.commons.client.monitor.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.weibo.api.commons.client.monitor.MonitorEventBuilder;

/**
 * 
 * Record access log.
 * Set the logger of "com.weibo.api.commons.client.listerner.AccessListener" to be trace if you want to record access log.
 * 
 *
 * @author fishermen
 * @version V1.0 created at: 2013-4-11
 */
@Component("accessListener")
public class AccessListener implements ClientListener {

	private static Logger log = LoggerFactory.getLogger(AccessListener.class);
	
	private boolean enableLogAccess = false;
	
	@Override
	public void onProcessComplete(MonitorEventBuilder eventClone) {
		//只有在测试时才会打开access日志
		if(enableLogAccess && log.isDebugEnabled()){
			log.debug(eventClone.toString());
		}
		
		//如果超时，进行日志记录
		if(eventClone.isTimeout() && log.isInfoEnabled()){
			log.info(String.format("Client operation timeout: %s", eventClone));
		}
	}

	public void setEnableLogAccess(boolean enableLogAccess) {
		this.enableLogAccess = enableLogAccess;
	}
	
	
}
